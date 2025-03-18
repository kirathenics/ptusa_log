package org.example.ptusa_log.listeners;

import org.example.ptusa_log.models.LogFile;

public interface LogDeleteListener {
    void onItemDeleted(LogFile logFile);
}
