package org.example.ptusa_log.helpers.filters;

import org.example.ptusa_log.models.Session;

import java.time.LocalDate;

public class DateFilter implements LogFilter {
    private final LocalDate date;

    public DateFilter(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean apply(Session session) {
//        return session.getDate().isAfter(date.atStartOfDay());
        return true;
    }
}