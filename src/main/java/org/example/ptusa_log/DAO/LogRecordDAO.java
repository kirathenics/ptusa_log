package org.example.ptusa_log.DAO;

import org.example.ptusa_log.models.LogRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogRecordDAO {
    private static final List<LogRecord> logRecordList = new ArrayList<>(Arrays.asList(
            new LogRecord("2025-03-06", "20.31.10", "INFO", "Started!"),
            new LogRecord("2025-03-06", "20.31.12", "WARNING", "Do something please"),
            new LogRecord("2025-03-06", "20.31.13", "ERROR", "Something went wrong"),
            new LogRecord("2025-03-06", "20.31.14", "INFO", "Finished!"),
            new LogRecord("2025-03-06", "20.31.18", "INFO", "Finished!")
    ));

    public static List<LogRecord> loadRecords() {
        return logRecordList;
    }

    public static List<LogRecord> filterRecords(String logType) {
        return logRecordList.stream()
                .filter(record -> logType.equals(record.getPriority()))
                .toList();
    }
}
