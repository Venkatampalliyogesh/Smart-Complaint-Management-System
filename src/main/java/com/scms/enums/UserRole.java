package com.scms.enums;

public enum UserRole {

    ROLE_USER("Citizen"),
    ROLE_STAFF("Staff"),
    ROLE_ADMIN("Administrator");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isAdmin() {
        return this == ROLE_ADMIN;
    }

    public boolean isStaff() {
        return this == ROLE_STAFF;
    }

    public boolean isUser() {
        return this == ROLE_USER;
    }
}