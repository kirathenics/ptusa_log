package org.example.ptusa_log.DAO;

import org.example.ptusa_log.utils.SQLReader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDatabaseManager {
    private static final String DB_FILE_NAME = "logs_data.db";
    private static final String DB_PATH = System.getProperty("user.home") +
            File.separator +
            "ptusa_log" +
            File.separator +
            "data" +
            File.separator +
            DB_FILE_NAME;

    private static final String JDBC_URL = "jdbc:sqlite:" + DB_PATH;

    static {
//        String envPath = System.getenv("DB_PATH");
//        DB_PATH = (envPath != null) ? envPath : "database/development.db";
//        System.out.println(DB_PATH);

        File dbFile = new File(DB_PATH);
        if (!dbFile.exists()) {
            try {
                Files.createDirectories(Paths.get(dbFile.getParent()));
            } catch (Exception e) {
                throw new RuntimeException("Ошибка создания директории для БД!", e);
            }
        }

        initializeDatabase();
    }

    private SQLiteDatabaseManager() {}

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }

    private static void initializeDatabase() {
        createLogPrioritiesTable();
        initializeLogPrioritiesTable();

        createVisibilitiesTable();
        initializeVisibilitiesTable();

        createSessionsTable();

        createMainSearchHistoryTable();
        createSessionSearchHistoryTable();

        //        applyMigrations();
    }

    private static void createLogPrioritiesTable() {
        String schemaFile = "log_priorities_table_schema.sql";

        String createTableSql = SQLReader.readSQLFileFromResource(schemaFile);

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка создания таблицы log_priorities", e);
        }
    }

    private static void initializeLogPrioritiesTable() {
        String dataFile = "log_priorities_data.sql";

        String insertDefaultsSql = SQLReader.readSQLFileFromResource(dataFile);

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(insertDefaultsSql);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка инициализации таблицы log_priorities", e);
        }
    }

    private static void createVisibilitiesTable() {
        String schemaFile = "visibilities_table_schema.sql";

        String createTableSql = SQLReader.readSQLFileFromResource(schemaFile);

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка создания таблицы visibilities_table", e);
        }
    }

    private static void initializeVisibilitiesTable() {
        String schemaFile = "visibilities_table_schema.sql";

        String createTableSql = SQLReader.readSQLFileFromResource(schemaFile);

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка создания таблицы visibilities_table", e);
        }
    }

    private static void createSessionsTable() {
        String schemaFile = "sessions_table_schema.sql";

        String createTableSql = SQLReader.readSQLFileFromResource(schemaFile);

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка создания таблицы sessions", e);
        }
    }

    private static void createMainSearchHistoryTable() {
        String schemaFile = "main_search_history_table_schema.sql";

        String createTableSql = SQLReader.readSQLFileFromResource(schemaFile);

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка создания таблицы main_search_history", e);
        }
    }

    private static void createSessionSearchHistoryTable() {
        String schemaFile = "session_search_history_table_schema.sql";

        String createTableSql = SQLReader.readSQLFileFromResource(schemaFile);

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка создания таблицы session_search_history", e);
        }
    }


//    private static void applyMigrations() {
//        List<String> migrations = List.of(
////                "ALTER TABLE log_files ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
//                "ALTER TABLE log_files RENAME COLUMN is_deleted TO visibility"
//        );
//
//        try (Connection conn = connect();
//             Statement stmt = conn.createStatement()) {
//            for (String sql : migrations) {
//                try {
//                    stmt.execute(sql);
//                } catch (SQLException ignored) {} // Если колонка уже существует — просто игнорируем
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException("Ошибка применения миграций", e);
//        }
//    }
}
