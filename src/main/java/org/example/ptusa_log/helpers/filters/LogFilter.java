package org.example.ptusa_log.helpers.filters;

import org.example.ptusa_log.models.Session;

@FunctionalInterface
public interface LogFilter {
    boolean apply(Session session);
}
