package com.redwerk.likelabs.application.dto.statistics;

public class Totals {
    
    private final int all;
    
    private final int last24Hours;
    
    private final int lastWeek;
    
    private final int lastMonth;

    public Totals(int all, int last24Hours, int lastWeek, int lastMonth) {
        this.all = all;
        this.last24Hours = last24Hours;
        this.lastWeek = lastWeek;
        this.lastMonth = lastMonth;
    }

    public int getAll() {
        return all;
    }

    public int getLast24Hours() {
        return last24Hours;
    }

    public int getLastWeek() {
        return lastWeek;
    }

    public int getLastMonth() {
        return lastMonth;
    }

}
