package org.example.ptusa_log.DAO;

import org.example.ptusa_log.models.Session;
import org.example.ptusa_log.utils.SessionProcessor;
import org.example.ptusa_log.utils.enums.LogFileVisibility;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionsDAO {
    private SessionsDAO() {}

    public static List<Session> getAllSessions() {
        List<Session> sessionList = new ArrayList<>();
        String query = "SELECT * FROM sessions";

        try (Connection conn = SQLiteDatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                sessionList.add(new Session(
                        rs.getInt("id"),
                        rs.getString("path"),
                        rs.getString("alias_name"),
                        rs.getString("device_name"),
                        rs.getInt("visibility"),
                        rs.getString("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sessionList;
    }

    public static List<Session> getSessions() {
        List<Session> sessionList = new ArrayList<>();
        String query = "SELECT * FROM sessions ORDER BY alias_name DESC";

        try (Connection conn = SQLiteDatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                sessionList.add(new Session(
                        rs.getInt("id"),
                        rs.getString("path"),
                        rs.getString("alias_name"),
                        rs.getString("device_name"),
                        rs.getInt("visibility"),
                        rs.getString("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sessionList;
    }

    public static void addSession(String path, String aliasName, String deviceName, Integer visibility, String created_at) {
        String query = "INSERT or IGNORE INTO sessions (path, alias_name, device_name, visibility, created_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = SQLiteDatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, path);
            stmt.setString(2, aliasName);
            stmt.setString(3, deviceName);
            stmt.setInt(4, visibility);
            stmt.setString(5, created_at);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setSessionAliasName(Integer id, String aliasName) {
        String query = "UPDATE sessions SET alias_name = ? WHERE id = ?";

        try (Connection conn = SQLiteDatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, aliasName);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setSessionVisibility(Integer id, Integer visibility) {
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

    public static void insertOrUpdateSession(String filePath) {
        Session session = findSessionByFilePath(filePath);
        if (session == null) {
            Path path = Paths.get(filePath);
            addSession(filePath,
                    SessionProcessor.extractAliasName(path),
                    SessionProcessor.extractDeviceName(path),
                    LogFileVisibility.VISIBLE.getValue(),
                    SessionProcessor.extractTimestamp(path)
                    );
        } else {
            setSessionVisibility(session.getId(), LogFileVisibility.VISIBLE.getValue());
        }
    }

    public static void removeDeletedSessionsFromFolder() {
        List<Session> sessions = getSessions();

        try (Connection conn = SQLiteDatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM sessions WHERE path = ?")) {

            for (Session session : sessions) {
                if (!Files.exists(Paths.get(session.getPath()))) {
                    stmt.setString(1, session.getPath());
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Session findSessionByFilePath(String filePath) {
        String query = "SELECT * FROM sessions WHERE path = ?";

        try (Connection conn = SQLiteDatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, filePath);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Session(
                        rs.getInt("id"),
                        rs.getString("path"),
                        rs.getString("alias_name"),
                        rs.getString("device_name"),
                        rs.getInt("visibility"),
                        rs.getString("created_at")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
