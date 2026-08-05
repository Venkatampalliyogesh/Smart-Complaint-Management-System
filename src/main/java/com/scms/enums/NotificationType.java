package com.scms.enums;

public enum NotificationType {

    COMPLAINT_CREATED("Complaint Created"),

    COMPLAINT_ASSIGNED("Complaint Assigned"),

    COMPLAINT_UPDATED("Complaint Updated"),

    COMPLAINT_RESOLVED("Complaint Resolved"),

    COMPLAINT_REJECTED("Complaint Rejected"),

    SYSTEM("System Notification"),

    REMINDER("Reminder");

    private final String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isComplaintNotification() {
        return this == COMPLAINT_CREATED
                || this == COMPLAINT_ASSIGNED
                || this == COMPLAINT_UPDATED
                || this == COMPLAINT_RESOLVED
                || this == COMPLAINT_REJECTED;
    }

    public boolean isSystemNotification() {
        return this == SYSTEM;
    }

    public boolean isReminder() {
        return this == REMINDER;
    }
}