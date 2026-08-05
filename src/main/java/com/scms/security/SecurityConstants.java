package com.scms.security;

public final class SecurityConstants {

    private SecurityConstants() {
    }

    // ==========================
    // JWT
    // ==========================
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER_STRING = "Authorization";

    // ==========================
    // PUBLIC URLS
    // ==========================
    public static final String[] PUBLIC_URLS = {

            "/",
            "/landing",
            "/login",
            "/register",
            "/forgot-password",

            "/api/auth/**",

            "/css/**",
            "/js/**",
            "/images/**",

            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",

            "/error",
            "/favicon.ico"
    };

    // ==========================
    // ROLE URLS
    // ==========================
    public static final String ADMIN_URL_PATTERN = "/api/admin/**";

    public static final String USER_URL_PATTERN = "/api/user/**";

    // ==========================
    // ROLES
    // ==========================
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static final String ROLE_USER = "ROLE_USER";

    public static final String ROLE_STAFF = "ROLE_STAFF";

}