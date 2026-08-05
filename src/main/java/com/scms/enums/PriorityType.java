package com.scms.enums;

public enum PriorityType {

    LOW("Low", 1),
    MEDIUM("Medium", 2),
    HIGH("High", 3),
    CRITICAL("Critical", 4);

    private final String displayName;
    private final int level;

    PriorityType(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLevel() {
        return level;
    }

    public boolean isLow() {
        return this == LOW;
    }

    public boolean isMedium() {
        return this == MEDIUM;
    }

    public boolean isHigh() {
        return this == HIGH;
    }

    public boolean isCritical() {
        return this == CRITICAL;
    }
}