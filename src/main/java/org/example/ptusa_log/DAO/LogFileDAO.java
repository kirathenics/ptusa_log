package org.example.ptusa_log.DAO;

import org.example.ptusa_log.models.LogFile;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogFileDAO {
    private LogFileDAO() {}

    public static List<LogFile> getLogFiles() {
        List<LogFile> logs = new ArrayList<>();
        String sql = "SELECT * FROM log_files WHERE is_deleted = 0 ORDER BY created_at DESC";

        try (Connection conn = SQLiteDatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                logs.add(new LogFile(
//                        rs.getInt("id"),
                        rs.getString("path"),
                        rs.getString("device_name")
//                        rs.getString("display_name"),
//                        rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }

    public static void addLogFile(String path, String displayName) {
        String sql = "INSERT INTO log_files (path, display_name) VALUES (?, ?)";

        try (Connection conn = SQLiteDatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, path);
            stmt.setString(2, displayName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
