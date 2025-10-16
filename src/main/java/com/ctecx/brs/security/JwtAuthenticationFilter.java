package com.ctecx.brs.security;


import com.ctecx.brs.constant.JWTConstants;
import com.ctecx.brs.mastertenant.config.DBContextHolder;
import com.ctecx.brs.mastertenant.entity.MasterTenant;
import com.ctecx.brs.mastertenant.service.MasterTenantService;
import com.ctecx.brs.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MasterTenantService masterTenantService;

    // Define paths that should bypass JWT authentication
    private static final List<String> BYPASS_PATHS = Arrays.asList(
            "/",
            "/index.html",
            "/static",
            "/assets",
            "/manifest.json",
            "/favicon.ico",
            "/logo192.png",
            "/logo512.png",
            "/api/auth/login",
            "/login",
            "/api/unauthenticated",
            "/actuator",
            "/swagger-ui",
            "/v3/api-docs",
            "/webjars"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getRequestURI();

        // Skip JWT processing for bypass paths
        if (shouldBypassJwtAuth(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = extractToken(request);
            if (token != null) {
                processToken(token, request);
            }
        } catch (ExpiredJwtException ex) {
            handleJwtException(request, response, JwtAuthenticationException.TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED);
            return;
        } catch (SignatureException ex) {
            handleJwtException(request, response, JwtAuthenticationException.INVALID_SIGNATURE, HttpStatus.UNAUTHORIZED);
            return;
        } catch (JwtException | BadCredentialsException ex) {
            handleJwtException(request, response, JwtAuthenticationException.INVALID_TOKEN, HttpStatus.UNAUTHORIZED);
            return;
        } catch (Exception ex) {
            logger.error("Authentication error: ", ex);
            handleJwtException(request, response, JwtAuthenticationException.AUTHENTICATION_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldBypassJwtAuth(String requestPath) {
        return BYPASS_PATHS.stream().anyMatch(path ->
                requestPath.equals(path) ||
                        requestPath.startsWith(path + "/") ||
                        requestPath.endsWith(".js") ||
                        requestPath.endsWith(".css") ||
                        requestPath.endsWith(".map") ||
                        requestPath.endsWith(".ico") ||
                        requestPath.endsWith(".png") ||
                        requestPath.endsWith(".jpg") ||
                        requestPath.endsWith(".jpeg") ||
                        requestPath.endsWith(".gif") ||
                        requestPath.endsWith(".svg") ||
                        requestPath.endsWith(".json")
        );
    }

    private void handleJwtException(HttpServletRequest request,
                                    HttpServletResponse response,
                                    String message,
                                    HttpStatus status) throws IOException {
        logger.warn("JWT Authentication failed: {} for request: {}", message, request.getRequestURI());
        request.setAttribute("exception", message);
        response.sendError(status.value(), message);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(JWTConstants.HEADER_STRING);
        if (header == null || !header.startsWith(JWTConstants.TOKEN_PREFIX)) {
            return null;
        }
        return header.substring(JWTConstants.TOKEN_PREFIX.length()).trim();
    }

    private void processToken(String token, HttpServletRequest request) {
        if (token.isEmpty()) {
            throw new BadCredentialsException(JwtAuthenticationException.EMPTY_TOKEN);
        }

        String username = jwtTokenUtil.getUsernameFromToken(token);
        String audience = jwtTokenUtil.getAudienceFromToken(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            setTenantContext(audience);
            authenticateUser(username, token, request);
        }
    }

    private void setTenantContext(String audience) {
        if (audience == null || audience.trim().isEmpty()) {
            throw new BadCredentialsException(JwtAuthenticationException.INVALID_TENANT_INFO);
        }

        try {
            Integer tenantId = Integer.valueOf(audience);
            MasterTenant masterTenant = masterTenantService.findByClientId(tenantId)
                    .orElseThrow(() -> new BadCredentialsException(JwtAuthenticationException.INVALID_TENANT));

            if (masterTenant.getStatus() == null ||
                    masterTenant.getStatus().toUpperCase().equals("INACTIVE")) {
                throw new BadCredentialsException(JwtAuthenticationException.INACTIVE_TENANT);
            }

            DBContextHolder.setCurrentDb(masterTenant.getDbName());
        } catch (NumberFormatException ex) {
            throw new BadCredentialsException(JwtAuthenticationException.INVALID_TENANT_FORMAT);
        }
    }

    private void authenticateUser(String username, String token, HttpServletRequest request) {
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.validateToken(token, userDetails)) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("Authenticated user {} with roles {}", username, userDetails.getAuthorities());
        }
    }

    public static class JwtAuthenticationException {
        public static final String TOKEN_EXPIRED = "Authentication token has expired";
        public static final String INVALID_SIGNATURE = "Invalid token signature";
        public static final String INVALID_TOKEN = "Invalid authentication token";
        public static final String EMPTY_TOKEN = "Authorization token cannot be empty";
        public static final String INVALID_TENANT_INFO = "Invalid tenant information in token";
        public static final String INVALID_TENANT = "Invalid tenant specified";
        public static final String INVALID_TENANT_FORMAT = "Invalid tenant format";
        public static final String AUTHENTICATION_ERROR = "Authentication error occurred";
        public static final String INACTIVE_TENANT = "Tenant is inactive";

        private JwtAuthenticationException() {} // Prevent instantiation
    }
}