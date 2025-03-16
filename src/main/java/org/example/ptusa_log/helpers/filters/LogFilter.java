package org.example.ptusa_log.helpers.filters;

import org.example.ptusa_log.models.LogFile;

@FunctionalInterface
public interface LogFilter {
    boolean apply(LogFile logFile);
}
