package org.example.ptusa_log.DAO;

import org.example.ptusa_log.models.LogPriority;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LogPriorityDAO {
    private LogPriorityDAO() {}

//    private static final List<LogPriority> LOG_PRIORITY_LIST = new ArrayList<>(Arrays.asList(
//            new LogPriority(1, "ALERT", "#ff0000", 1),
//            new LogPriority(2, "CRITIC", "#ff0000", 2),
//            new LogPriority(3, "ERROR", "#ff0000", 3),
////                new LogType(4, "WARNING1", "#ffff00", 4),
//            new LogPriority(4, "WARNING", "#fed000", 4),
//            new LogPriority(5, "NOTICE", "#00ff00", 5),
//            new LogPriority(6, "INFO", "#00ff00", 6),
//            new LogPriority(7, "DEBUG", "#555555", 7)
//    ));

    public static List<LogPriority> getPriorities() {
        List<LogPriority> logPriorities = new ArrayList<>();
        String sql = "SELECT * FROM log_priorities ORDER BY id ASC";

        try (Connection conn = SQLiteDatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                logPriorities.add(new LogPriority(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("color"),
                        rs.getInt("priority")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logPriorities;
    }
}

/*

switch ( priority )
    {
    case i_log::P_ALERT:
        SetColor( RED_I );
        std::cout << "ALERT  (1) -> ";
        break;

    case i_log::P_CRIT:
        SetColor( RED_I );
        std::cout << "CRITIC (2) -> ";
        break;

    case i_log::P_ERR:
        SetColor( RED );
        std::cout << "ERROR  (3) -> ";
        break;

    case i_log::P_WARNING:
        SetColor( YELLOW );
        std::cout << "WARNING(4) -> ";
        break;

    case i_log::P_NOTICE:
        SetColor( GREEN );
        std::cout << "NOTICE (5) -> ";
        break;

    case i_log::P_INFO:
        SetColor( GREEN );
        std::cout << "INFO   (6) -> ";
        break;

    case i_log::P_DEBUG:
        SetColor( GRAY );
        std::cout << "DEBUG  (7) -> ";
        break;

 */