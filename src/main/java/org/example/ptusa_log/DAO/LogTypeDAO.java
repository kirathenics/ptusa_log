package org.example.ptusa_log.DAO;

import org.example.ptusa_log.models.LogType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogTypeDAO {
    private static final List<LogType> logTypeList = new ArrayList<>(Arrays.asList(
            new LogType(1, "ALERT", "#ff0000", 1),
            new LogType(2, "CRITIC", "#ff0000", 2),
            new LogType(3, "ERROR", "#ff0000", 3),
//                new LogType(4, "WARNING1", "#ffff00", 4),
            new LogType(4, "WARNING", "#fed000", 4),
            new LogType(5, "NOTICE", "#00ff00", 5),
            new LogType(6, "INFO", "#00ff00", 6),
            new LogType(7, "DEBUG", "#aaaaaa", 7)
    ));

    public static List<LogType> loadTypes() {
        return logTypeList;
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