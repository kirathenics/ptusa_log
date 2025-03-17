package org.example.ptusa_log.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class LogFileProcessor {
    public static String extractAliasName(Path path) {
        String fileName = path.getFileName().toString();
        if (fileName.startsWith("ptusa_") && fileName.endsWith(".log")) {
            return Constants.SESSION + fileName.substring(6, fileName.length() - 4);
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
        return "Неизвестно";
    }
}
