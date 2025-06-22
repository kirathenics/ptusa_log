package org.example.ptusa_log.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SessionProcessor {

    private static final String filePrefix = "ptusa_";
    private static final String filePostfix = ".log";

    public static String extractAliasName(Path path) {
        String fileName = path.getFileName().toString();

        if (fileName.startsWith(filePrefix) && fileName.endsWith(filePostfix)) {
            return StringConstants.SESSION + fileName.substring(filePrefix.length(), fileName.length() - filePostfix.length());
        }

        return fileName;
    }

    public static String extractDeviceName(Path path) {
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String firstLine = reader.readLine();
            if (firstLine != null && firstLine.startsWith("Device:")) {
                return firstLine.split(":", 2)[1].trim();
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + path.getFileName());
        }

        return StringConstants.UNKNOWN_DEVICE;
    }

    public static String extractTimestamp(Path path) {
        String fileName = path.getFileName().toString();

        if (fileName.startsWith(filePrefix) && fileName.endsWith(filePostfix)) {
            String timestampPart = fileName.substring(filePrefix.length(), fileName.length() - filePostfix.length());
            return timestampPart.replace('_', ' ').replaceAll("-", ":");
        }

        return null;
    }
}
