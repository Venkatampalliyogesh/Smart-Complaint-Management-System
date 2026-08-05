package com.scms.constants;

public final class SecurityConstants {

    private SecurityConstants() {
    }

    public static final String[] PUBLIC_URLS = {
            "/api/auth/**",
            "/",
            "/index.html",
            "/register.html",
            "/forgot-password.html",
            "/reset-password.html",
            "/profile.html",
            "/change-password.html",
            "/complaints.html",
            "/complaint-create.html",
            "/complaint-detail.html",
            "/complaint-track.html",
            "/admin-dashboard.html",
            "/manage-users.html",
            "/manage-complaints.html",
            "/css/**",
            "/js/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api-docs/**",
            "/v3/api-docs/**"
    };

    public static final String ADMIN_URL_PATTERN = "/api/admin/**";
}
