package com.scms.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static CustomUserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new IllegalStateException("No authenticated user found");
        }
        return (CustomUserDetails) authentication.getPrincipal();
    }

    public static String getCurrentUserEmail() {
        return getCurrentUserDetails().getUsername();
    }

    public static Long getCurrentUserId() {
        return getCurrentUserDetails().getId();
    }
}
