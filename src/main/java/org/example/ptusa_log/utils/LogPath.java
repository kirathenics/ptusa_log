package org.example.ptusa_log.utils;

public class LogPath {
    public static String defineLogPath() {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return "C:\\ProgramData\\ptusa\\logs";
        } else {
            return "/var/log/ptusa.log";
        }
    }
}
