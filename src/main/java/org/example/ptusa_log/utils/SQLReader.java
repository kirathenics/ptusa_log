package org.example.ptusa_log.utils;

import org.example.ptusa_log.DAO.SQLiteDatabaseManager;

import java.io.*;
import java.util.Objects;

public class SQLReader {
    public static String readSQLFromFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения SQL-файла: " + filePath, e);
        }
        return sb.toString();
    }

    public static String readSQLFileFromResource(String resourcePath) {
        StringBuilder sb = new StringBuilder();
        try (InputStream inputStream = SQLiteDatabaseManager.class.getResourceAsStream(StringConstants.DATABASE_PATH + resourcePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения SQL-файла из ресурсов: " + resourcePath, e);
        }
        return sb.toString();
    }
}
