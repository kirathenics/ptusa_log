package org.example.ptusa_log.DAO;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDatabaseManager {
//    private static final String DB_FILE_NAME = "extra_data.db";
//    private static final String DB_PATH = System.getProperty("user.home") +
//            File.separator +
//            "ptusa_log" +
//            File.separator +
//            DB_FILE_NAME;
//    private static final String URL = "jdbc:sqlite:" + DB_PATH;
//
//    static {
//        extractDatabase();
//    }
//
//    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(URL);
//    }
//
//    private static void extractDatabase() {
//        File dbFile = new File(DB_PATH);
//        if (!dbFile.exists()) {
//            try (InputStream is = SQLiteDatabaseManager.class.getResourceAsStream( Constants.PROJECT_PATH + DB_FILE_NAME)) {
//                if (is == null) {
//                    throw new RuntimeException("Файл базы данных не найден в ресурсах!");
//                }
//                Files.copy(is, dbFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//                System.out.println("База данных скопирована в " + DB_PATH);
//            } catch (IOException e) {
//                throw new RuntimeException("Ошибка копирования базы данных", e);
//            }
//        }
//    }

    private static final String DB_PATH;

    static {
        String envPath = System.getenv("DB_PATH");
        DB_PATH = (envPath != null) ? envPath : "database/development.db";

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

    private static final String JDBC_URL = "jdbc:sqlite:" + DB_PATH;

    private SQLiteDatabaseManager() {}

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }

    private static void initializeDatabase() {
        String sql = """
            CREATE TABLE IF NOT EXISTS log_files (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                path TEXT NOT NULL UNIQUE,
                display_name TEXT,
                device_name TEXT,
                is_deleted INTEGER DEFAULT 0,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
        """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка инициализации БД", e);
        }

//        applyMigrations();
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
