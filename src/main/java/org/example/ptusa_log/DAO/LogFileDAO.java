package org.example.ptusa_log.DAO;

import org.example.ptusa_log.models.LogFile;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogFileDAO {
    private LogFileDAO() {}

    public static List<LogFile> getLogFiles() {
        List<LogFile> logs = new ArrayList<>();
        String sql = "SELECT * FROM log_files WHERE is_deleted = 0 ORDER BY alias_name DESC";

        try (Connection conn = SQLiteDatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                logs.add(new LogFile(
                        rs.getInt("id"),
                        rs.getString("path"),
                        rs.getString("alias_name"),
                        rs.getString("device_name"),
                        rs.getInt("is_deleted")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }

    public static void addLogFile(String path, String aliasName, String deviceName, Integer isDeleted) {
        String sql = "INSERT or IGNORE INTO log_files (path, alias_name, device_name, is_deleted) VALUES (?, ?, ?, ?)";

        try (Connection conn = SQLiteDatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, path);
            stmt.setString(2, aliasName);
            stmt.setString(3, deviceName);
            stmt.setInt(4, isDeleted);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setAliasName(Integer id, String aliasName) {
        String sql = "UPDATE log_files SET alias_name = ? WHERE id = ?";

        try (Connection conn = SQLiteDatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, aliasName);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setLogFileDeletion(Integer id, Integer isDeleted) {
        String sql = "UPDATE log_files SET is_deleted = ? WHERE id = ?";

        try (Connection conn = SQLiteDatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, isDeleted);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
