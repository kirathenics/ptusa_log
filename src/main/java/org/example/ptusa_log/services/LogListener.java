package org.example.ptusa_log.services;

import org.example.ptusa_log.models.LogFile;

import java.util.List;

public interface LogListener {
    void onLogsUpdated(List<LogFile> logFiles);
}

