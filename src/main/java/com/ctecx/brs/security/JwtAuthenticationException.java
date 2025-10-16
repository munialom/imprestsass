package com.ctecx.brs.security;



public class JwtAuthenticationException {
    public static final String TOKEN_EXPIRED = "JWT token has expired";
    public static final String INVALID_SIGNATURE = "Invalid JWT signature";
    public static final String INVALID_TOKEN = "Invalid JWT token";
    public static final String AUTHENTICATION_ERROR = "Authentication error";
    public static final String INVALID_TENANT_INFO = "Invalid tenant information in token";
    public static final String INVALID_TENANT = "Invalid tenant";
    public static final String INVALID_TENANT_FORMAT = "Invalid tenant format";

    private JwtAuthenticationException() {
        // Private constructor to prevent instantiation
    }
}