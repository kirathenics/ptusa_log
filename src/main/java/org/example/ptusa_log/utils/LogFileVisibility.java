package org.example.ptusa_log.utils;

public enum LogFileVisibility {
    VISIBLE(0), DELETED(1), ARCHIVED(2);

    private final int value;

    LogFileVisibility(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

