package com.scms.util;

public final class AppConstants {

    private AppConstants() {
    }

    // =========================
    // APPLICATION
    // =========================
    public static final String APPLICATION_NAME = "Smart Complaint Management System";

    public static final String API_BASE_URL = "/api";

    // =========================
    // DEFAULT VALUES
    // =========================
    public static final int DEFAULT_PAGE_NUMBER = 0;

    public static final int DEFAULT_PAGE_SIZE = 10;

    public static final String DEFAULT_SORT_BY = "createdAt";

    public static final String DEFAULT_SORT_DIRECTION = "desc";

    // =========================
    // USER ROLES
    // =========================
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static final String ROLE_STAFF = "ROLE_STAFF";

    public static final String ROLE_USER = "ROLE_USER";

    // =========================
    // JWT
    // =========================
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String AUTHORIZATION_HEADER = "Authorization";

    // =========================
    // COMPLAINT
    // =========================
    public static final String TICKET_PREFIX = "SCMS-";

    public static final String DEFAULT_PRIORITY = "MEDIUM";

    public static final String DEFAULT_STATUS = "SUBMITTED";

    // =========================
    // SUCCESS MESSAGES
    // =========================
    public static final String LOGIN_SUCCESS = "Login Successful";

    public static final String REGISTER_SUCCESS = "Registration Successful";

    public static final String LOGOUT_SUCCESS = "Logout Successful";

    public static final String PROFILE_UPDATED = "Profile Updated Successfully";

    public static final String PASSWORD_CHANGED = "Password Changed Successfully";

    public static final String COMPLAINT_CREATED = "Complaint Submitted Successfully";

    public static final String COMPLAINT_UPDATED = "Complaint Updated Successfully";

    public static final String COMPLAINT_ASSIGNED = "Complaint Assigned Successfully";

    public static final String COMPLAINT_DELETED = "Complaint Deleted Successfully";

    // =========================
    // ERROR MESSAGES
    // =========================
    public static final String USER_NOT_FOUND = "User Not Found";

    public static final String COMPLAINT_NOT_FOUND = "Complaint Not Found";

    public static final String CATEGORY_NOT_FOUND = "Category Not Found";

    public static final String INVALID_CREDENTIALS = "Invalid Email or Password";

    public static final String ACCESS_DENIED = "Access Denied";

    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

}