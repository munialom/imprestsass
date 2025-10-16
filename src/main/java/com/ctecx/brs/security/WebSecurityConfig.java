package com.ctecx.brs.security;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * @author Md. Amran Hossain | Updated
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity

public class WebSecurityConfig {



    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    @Autowired
    private UnauthenticatedRequestFilter unauthenticatedRequestFilter;

  //  private  ApiRequestAuditFilter apiRequestAuditFilter;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter authenticationTokenFilterBean() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            unauthorizedHandler.commence(request, response, authException);
                        })
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers(
                                    "/",
                                    "/index.html",
                                    "/static/**",
                                    "/assets/**",
                                    "/manifest.json",
                                    "/favicon.ico",
                                    "/*.js",
                                    "/*.json",
                                    "/*.ico",
                                    "/*.png",
                                    "/logo192.png",
                                    "/logo512.png",
                                    "/api/auth/login",
                                    "/login", "/api/unauthenticated/**"
                            ).permitAll()
                            .requestMatchers(
                                    "/api/product/**",
                                    "/api/classes/**",
                                    "/api/streams/**",
                                    "/api/students/**",
                                    "/roles/**"

                            ).authenticated()
                            .anyRequest().permitAll(); // Allow all other requests
                });

                http.addFilterBefore(unauthenticatedRequestFilter, UsernamePasswordAuthenticationFilter.class);
                http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
                 //http.addFilterAfter(apiRequestAuditFilter, UsernamePasswordAuthenticationFilter.class); // 👈 audit comes AFTER JWT auth

        return http.build();
    }




    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept"
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> bean =
                new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(-110);
        return bean;
    }
}