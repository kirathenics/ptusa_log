package org.example.ptusa_log.DAO;

import org.example.ptusa_log.models.LogFile;
import org.example.ptusa_log.utils.LogFileProcessor;
import org.example.ptusa_log.utils.enums.LogFileVisibility;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogFileDAO {
    private LogFileDAO() {}

    public static List<LogFile> getLogFiles() {
        List<LogFile> logs = new ArrayList<>();
//        String query = "SELECT * FROM log_files WHERE visibility = 0 ORDER BY alias_name DESC";
        String query = "SELECT * FROM log_files ORDER BY alias_name DESC";

        try (Connection conn = SQLiteDatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                logs.add(new LogFile(
                        rs.getInt("id"),
                        rs.getString("path"),
                        rs.getString("alias_name"),
                        rs.getString("device_name"),
                        rs.getInt("visibility")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }

    public static void addLogFile(String path, String aliasName, String deviceName, Integer visibility) {
        String query = "INSERT or IGNORE INTO log_files (path, alias_name, device_name, visibility) VALUES (?, ?, ?, ?)";

        try (Connection conn = SQLiteDatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, path);
            stmt.setString(2, aliasName);
            stmt.setString(3, deviceName);
            stmt.setInt(4, visibility);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setLogFileAliasName(Integer id, String aliasName) {
        String query = "UPDATE log_files SET alias_name = ? WHERE id = ?";

        try (Connection conn = SQLiteDatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, aliasName);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setLogFileVisibility(Integer id, Integer visibility) {
        String query = "UPDATE log_files SET visibility = ? WHERE id = ?";

        try (Connection conn = SQLiteDatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, visibility);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertOrUpdateFile(String filePath) {
        LogFile logFile = findLogFileByFilePath(filePath);
        if (logFile == null) {
            Path path = Paths.get(filePath);
            addLogFile(filePath,
                    LogFileProcessor.extractAliasName(path),
                    LogFileProcessor.extractDeviceName(path),
                    LogFileVisibility.VISIBLE.getValue());
        } else {
            setLogFileVisibility(logFile.getId(), LogFileVisibility.VISIBLE.getValue());
        }
    }

    public static void removeDeletedLogFiles() {
        List<LogFile> logFiles = getLogFiles();

        try (Connection conn = SQLiteDatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM log_files WHERE path = ?")) {

            for (LogFile log : logFiles) {
                if (!Files.exists(Paths.get(log.getPath()))) {
                    stmt.setString(1, log.getPath());
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static LogFile findLogFileByFilePath(String filePath) {
        String query = "SELECT * FROM log_files WHERE path = ?";

        try (Connection conn = SQLiteDatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, filePath);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new LogFile(
                        rs.getInt("id"),
                        rs.getString("path"),
                        rs.getString("alias_name"),
                        rs.getString("device_name"),
                        rs.getInt("visibility")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
