package com.redwerk.likelabs.application.dto;

import java.util.List;

public class Report<T> {
    
    private final List<T> items;
    
    private final int count;

    public Report(List<T> items, int count) {
        this.items = items;
        this.count = count;
    }

    public List<T> getItems() {
        return items;
    }

    public int getCount() {
        return count;
    }

}
