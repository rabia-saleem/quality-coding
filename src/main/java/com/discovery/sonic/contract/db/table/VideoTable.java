package com.discovery.sonic.contract.db.table;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Mock db integration.
 *
 * You should not need to change this class.
 */
public class VideoTable {
    public static final int TITLE_WIDTH = 55;
    private final Map<Object, Object[]> videos = new ConcurrentHashMap<>();

    public Object[] readRow(Object id) {
        var row = videos.get(id);
        var copy = Arrays.copyOf(row, 3);
        // Hide atomic long implementation detail, and instead return raw data
        copy[2] = ((AtomicLong) row[2]).get();
        return copy;
    }

    public void writeRow(Object[] row) {
        if (row.length < 2 || row[1] == null) {
            throw new NotNullConstraintException("Not null constraint on title column");
        }
        var title = row[1].toString();
        column(title);
        var copy = Arrays.copyOf(row, 3);
        copy[2] = new AtomicLong();
        videos.put(row[0], copy);
    }

    private void column(String title) {
        if (title.length() > TITLE_WIDTH) {
            throw new ColumnWidthExceededException("Title column has max width of " + TITLE_WIDTH);
        }
    }

    public void incrementCounterInRow(String id) {
        videos.computeIfPresent(id, (key, row) -> {
            var counter = (AtomicLong) row[2];
            counter.incrementAndGet();
            return row;
        });
    }

    public static class NotNullConstraintException extends RuntimeException {
        public NotNullConstraintException(String message) {
            super(message);
        }
    }

    public static class ColumnWidthExceededException extends RuntimeException {
        public ColumnWidthExceededException(String message) {
            super(message);
        }
    }
}
