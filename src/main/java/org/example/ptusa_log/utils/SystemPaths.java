package org.example.ptusa_log.utils;

public class SystemPaths {
    public static String defineLogFilesPath() {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return "C:\\ProgramData\\ptusa\\logs";
        } else {
            return "/var/log/ptusa.log";
        }
    }
}
