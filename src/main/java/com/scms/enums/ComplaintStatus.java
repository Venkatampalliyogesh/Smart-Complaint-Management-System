package com.scms.enums;

public enum ComplaintStatus {

    SUBMITTED("Submitted"),
    ASSIGNED("Assigned"),
    IN_PROGRESS("In Progress"),
    RESOLVED("Resolved"),
    CLOSED("Closed"),
    REJECTED("Rejected");

    private final String displayName;

    ComplaintStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isOpen() {
        return this == SUBMITTED
                || this == ASSIGNED
                || this == IN_PROGRESS;
    }

    public boolean isCompleted() {
        return this == RESOLVED
                || this == CLOSED;
    }

    public boolean isRejected() {
        return this == REJECTED;
    }
}