/*

package com.ctecx.rentals.security;

import com.ctecx.rentals.mastertenant.config.DBContextHolder;
import com.ctecx.rentals.mastertenant.entity.MasterTenant;
import com.ctecx.rentals.mastertenant.service.MasterTenantService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class UnauthenticatedRequestFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(UnauthenticatedRequestFilter.class);

    @Autowired
    private MasterTenantService masterTenantService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // Skip filter if not an IPN POST request
        if (!requestURI.startsWith("/api/v1/ipn") || !"POST".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String siteUrl = getSiteUrl(request);
        Optional<MasterTenant> optionalTenant = masterTenantService.findByDomainUrl(siteUrl);

        if (optionalTenant.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tenant not found");
            return;
        }

        MasterTenant tenant = optionalTenant.get();
        String currentThread = Thread.currentThread().getName();

        try {
            LOG.debug("Thread: {} - Setting tenant DB: {}", currentThread, tenant.getDbName());
            DBContextHolder.setCurrentDb(tenant.getDbName());

            // Proceed with the request
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            LOG.error("Thread: {} - Error processing request: {}", currentThread, e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        } finally {
            // Ensure context is cleared even if an exception occurs
            DBContextHolder.clear();
            LOG.debug("Thread: {} - Cleared tenant context", currentThread);
        }
    }

    private String getSiteUrl(HttpServletRequest request) {
        String siteUrl = request.getRequestURL().toString();
        return siteUrl.replace(request.getServletPath(), "");
    }
}*/


package com.ctecx.brs.security;


import com.ctecx.brs.mastertenant.config.DBContextHolder;
import com.ctecx.brs.mastertenant.entity.MasterTenant;
import com.ctecx.brs.mastertenant.service.MasterTenantService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class UnauthenticatedRequestFilter extends OncePerRequestFilter {

    @Autowired
    private MasterTenantService masterTenantService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String requestId = "REQ-" + System.currentTimeMillis();

        try {
            // Skip filter if not an IPN POST request
            if (!shouldProcessRequest(request)) {
                log.debug("Skipping filter for non-IPN request [{}] - {}", requestId, requestURI);
                filterChain.doFilter(request, response);
                return;
            }

            processIpnRequest(request, response, filterChain, requestId);
            log.debug("Successfully processed IPN request [{}]", requestId);

        } catch (TenantNotFoundException e) {
            log.warn("Tenant not found for request [{}] - {}", requestId, e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (RequestProcessingException e) {
            log.error("Error processing IPN request [{}] - {}", requestId, e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            DBContextHolder.clear();
            log.debug("Cleared tenant context for request [{}]", requestId);
        }
    }

    private boolean shouldProcessRequest(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.startsWith("/api/v1/ipn") && "POST".equalsIgnoreCase(request.getMethod());
    }

    private void processIpnRequest(HttpServletRequest request, HttpServletResponse response,
                                   FilterChain filterChain, String requestId) throws IOException, ServletException {
        String siteUrl = getSiteUrl(request);
        log.debug("Processing IPN request [{}] - URL: {}", requestId, siteUrl);

        MasterTenant tenant = masterTenantService.findByDomainUrl(siteUrl)
                .orElseThrow(() -> new TenantNotFoundException("Tenant not found for URL: " + siteUrl));

        log.debug("Setting tenant DB for request [{}] - Tenant: {}", requestId, tenant.getDbName());
        DBContextHolder.setCurrentDb(tenant.getDbName());

        filterChain.doFilter(request, response);
    }

    private String getSiteUrl(HttpServletRequest request) {
        String siteUrl = request.getRequestURL().toString();
        return siteUrl.replace(request.getServletPath(), "");
    }

    // Custom Exception Classes
    public static class RequestProcessingException extends RuntimeException {
        public RequestProcessingException(String message) {
            super(message);
        }
        public RequestProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class TenantNotFoundException extends RuntimeException {
        public TenantNotFoundException(String message) {
            super(message);
        }
    }
}