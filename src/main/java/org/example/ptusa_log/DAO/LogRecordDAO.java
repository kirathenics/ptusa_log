package org.example.ptusa_log.DAO;

import org.example.ptusa_log.models.LogRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogRecordDAO {
    public static List<LogRecord> loadRecords() {
        return new ArrayList<>(Arrays.asList(
                new LogRecord(1, "06-03-2025", "20:31:10", "INFO", "Started!"),
                new LogRecord(2, "06-03-2025", "20:31:11", "INFO", "Finished!")
        ));
    }
}
