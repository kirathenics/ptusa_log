package org.example.ptusa_log.DAO;

import org.example.ptusa_log.models.MainSearchHistoryQuery;
import org.example.ptusa_log.models.SessionSearchHistoryQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SessionSearchHistoryDAO {

    public static List<SessionSearchHistoryQuery> getSearchHistory() {
        String sql = """
            SELECT * FROM session_search_history
        """;

        try (Connection conn = SQLiteDatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            List<SessionSearchHistoryQuery> results = new ArrayList<>();
            while (rs.next()) {
                results.add(new SessionSearchHistoryQuery(
                        rs.getInt("id"),
                        rs.getString("query"),
                        rs.getInt("session_id"),
                        rs.getString("created_at")
                ));
            }

            return results;
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public static List<SessionSearchHistoryQuery> getSearchHistory(String query, int session_id, int limit) {
        String sql = """
            WITH primary_queries AS (
                SELECT * FROM session_search_history
                WHERE session_id = ?
                  AND query LIKE ?
                GROUP BY query
                ORDER BY created_at DESC
                LIMIT ?
            ),
            secondary_queries AS (
                SELECT * FROM session_search_history
                WHERE session_id != ?
                  AND query LIKE ?
                  AND query NOT IN (SELECT query FROM primary_queries)
                GROUP BY query
                ORDER BY created_at DESC
                LIMIT ?
            )
            SELECT * FROM primary_queries
            UNION ALL
            SELECT * FROM secondary_queries
            ORDER BY created_at DESC
            LIMIT ?
        """;

        try (Connection conn = SQLiteDatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, session_id);
            stmt.setString(2, "%" + query + "%");
            stmt.setInt(3, limit);

            stmt.setInt(4, session_id);
            stmt.setString(5, "%" + query + "%");
            stmt.setInt(6, limit);

            stmt.setInt(7, limit); // общий лимит

            ResultSet rs = stmt.executeQuery();

            List<SessionSearchHistoryQuery> results = new ArrayList<>();
            while (rs.next()) {
                results.add(new SessionSearchHistoryQuery(
                        rs.getInt("id"),
                        rs.getString("query"),
                        rs.getInt("session_id"),
                        rs.getString("created_at")
                ));
            }

            return results;
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public static boolean findByQueryAndSessionId(String query, int session_id) {
        String checkQuery = "SELECT 1 FROM session_search_history WHERE query = ? AND session_id = ?";

        try (Connection conn = SQLiteDatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(checkQuery)) {

            stmt.setString(1, query);
            stmt.setInt(2, session_id);
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void addQuery(String query, int session_id) {
        String insertQuery = "INSERT INTO session_search_history (query, session_id) VALUES (?, ?)";

        try (Connection conn = SQLiteDatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setString(1, query);
            stmt.setInt(2, session_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteHistoryQuery(String query, int session_id) {
        String deleteQuery = "DELETE FROM session_search_history WHERE query = ? AND session_id = ?";

        try (Connection conn = SQLiteDatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {

            stmt.setString(1, query);
            stmt.setInt(2, session_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
