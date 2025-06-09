package org.example.ptusa_log.DAO;

import org.example.ptusa_log.models.MainSearchHistoryQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainSearchHistoryDAO {

    public static List<MainSearchHistoryQuery> getSearchHistory() {
        String sql = """
            SELECT * FROM main_search_history
        """;

        try (Connection conn = SQLiteDatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            List<MainSearchHistoryQuery> results = new ArrayList<>();
            while (rs.next()) {
                results.add(new MainSearchHistoryQuery(
                        rs.getInt("id"),
                        rs.getString("query"),
                        rs.getInt("visibility"),
                        rs.getString("created_at")
                ));
            }

            return results;
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public static List<MainSearchHistoryQuery> getSearchHistory(String query, int visibility, int limit) {
        String sql = """
            WITH primary_queries AS (
                SELECT * FROM main_search_history
                WHERE visibility = ?
                  AND query LIKE ?
                GROUP BY query
                ORDER BY created_at DESC
                LIMIT ?
            ),
            secondary_queries AS (
                SELECT * FROM main_search_history
                WHERE visibility != ?
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

            stmt.setInt(1, visibility);
            stmt.setString(2, "%" + query + "%");
            stmt.setInt(3, limit);

            stmt.setInt(4, visibility);
            stmt.setString(5, "%" + query + "%");
            stmt.setInt(6, limit);

            stmt.setInt(7, limit); // общий лимит

            ResultSet rs = stmt.executeQuery();

            List<MainSearchHistoryQuery> results = new ArrayList<>();
            while (rs.next()) {
                results.add(new MainSearchHistoryQuery(
                        rs.getInt("id"),
                        rs.getString("query"),
                        rs.getInt("visibility"),
                        rs.getString("created_at")
                ));
            }

            return results;
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public static boolean findByQueryAndVisibility(String query, int visibility) {
        String checkQuery = "SELECT 1 FROM main_search_history WHERE query = ? AND visibility = ?";

        try (Connection conn = SQLiteDatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(checkQuery)) {

            stmt.setString(1, query);
            stmt.setInt(2, visibility);
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void addQuery(String query, int visibility) {
        String insertQuery = "INSERT INTO main_search_history (query, visibility) VALUES (?, ?)";

        try (Connection conn = SQLiteDatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setString(1, query);
            stmt.setInt(2, visibility);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteHistoryQuery(String query, int visibility) {
        String deleteQuery = "DELETE FROM main_search_history WHERE query = ? AND visibility = ?";

        try (Connection conn = SQLiteDatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {

            stmt.setString(1, query);
            stmt.setInt(2, visibility);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
