package com.redwerk.likelabs.application.dto;

public class Pager {
    
    public static final Pager ALL_RECORDS = new Pager(0, Integer.MAX_VALUE);
    
    private final int offset;
    
    private final int count;

    public Pager(int offset, int count) {
        if (offset < 0 || count < 0) {
            throw new IllegalArgumentException();
        }
        this.offset = offset;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public int getOffset() {
        return offset;
    }
    
}
