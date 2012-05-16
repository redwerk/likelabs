package com.redwerk.likelabs.application.dto;

public class Pager {
    
    public static final Pager ALL_RECORDS = new Pager(0, Integer.MAX_VALUE);
    
    private final int offset;
    
    private final int limit;

    public Pager(int offset, int limit) {
        if (offset < 0 || limit < 0) {
            throw new IllegalArgumentException();
        }
        this.offset = offset;
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }
    
}
