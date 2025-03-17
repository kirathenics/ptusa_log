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
//                dbFile.getParentFile().mkdirs();
//                dbFile.createNewFile();
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
        initializeLogFilesTable();
        initializeLofPrioritiesTable();

//        applyMigrations();
    }

//    private static void initializeLogFilesTable() {
//        String sql = """
//            CREATE TABLE IF NOT EXISTS log_files (
//                id INTEGER PRIMARY KEY AUTOINCREMENT,
//                path TEXT NOT NULL UNIQUE,
//                alias_name TEXT NOT NULL,
//                device_name TEXT NOT NULL,
//                is_deleted INTEGER NOT NULL DEFAULT 0
//            );
//        """;
//
//        try (Connection conn = connect();
//             Statement stmt = conn.createStatement()) {
//            stmt.execute(sql);
//        } catch (SQLException e) {
//            throw new RuntimeException("Ошибка инициализации БД", e);
//        }
//    }

    private static void initializeLogFilesTable() {
        String schemaFile = "log_files_table_schema.sql";

        String createTableSql = SQLReader.readSQLFileFromResource(schemaFile);

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка инициализации БД", e);
        }
    }

    private static void initializeLofPrioritiesTable() {
        String schemaFile = "log_priorities_table_schema.sql";
        String dataFile = "log_priorities_data.sql";

        String createTableSql = SQLReader.readSQLFileFromResource(schemaFile);
        String insertDefaultsSql = SQLReader.readSQLFileFromResource(dataFile);

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSql);
            stmt.execute(insertDefaultsSql);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка инициализации БД", e);
        }
    }


//    private static void applyMigrations() {
//        List<String> migrations = List.of(
//                "ALTER TABLE log_files ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
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

