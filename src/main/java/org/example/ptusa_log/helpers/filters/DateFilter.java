package org.example.ptusa_log.helpers.filters;

import org.example.ptusa_log.models.LogFile;

import java.time.LocalDate;

public class DateFilter implements LogFilter {
    private final LocalDate date;

    public DateFilter(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean apply(LogFile logFile) {
//        return logFile.getDate().isAfter(date.atStartOfDay());
        return true;
    }
}