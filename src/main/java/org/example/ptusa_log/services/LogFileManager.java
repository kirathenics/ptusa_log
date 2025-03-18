package org.example.ptusa_log.services;

import org.example.ptusa_log.DAO.LogFileDAO;
import org.example.ptusa_log.models.LogFile;
import org.example.ptusa_log.utils.LogFileProcessor;
import org.example.ptusa_log.utils.SystemPaths;

import java.nio.file.Paths;
import java.util.List;

public class LogFileManager {
    private final String logsPath = SystemPaths.defineLogFilesPath();

    public String getLogsPath() {
        return logsPath;
    }

    public List<LogFile> getLogFiles() {
        return LogFileDAO.getLogFiles();
    }

    public void addLogFile(String filePath) {
        String aliasName = LogFileProcessor.extractAliasName(Paths.get(filePath));
        String deviceName = LogFileProcessor.extractDeviceName(Paths.get(filePath));
        LogFileDAO.addLogFile(filePath, aliasName, deviceName, 0);
    }

    public void deleteLogFile(LogFile logFile) {
        LogFileDAO.setLogFileDeletion(logFile.getId(), 1);
    }
}
