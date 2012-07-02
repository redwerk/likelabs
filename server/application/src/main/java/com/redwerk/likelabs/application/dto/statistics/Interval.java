package com.redwerk.likelabs.application.dto.statistics;

public enum Interval {

    MONTHS_12(365*24*60*60*1000L/30), MONTHS_6(365*24*60*60*1000L/60), DAYS_30(24*60*60*1000L);

    private final long millis;
    
    private Interval(long millis) {
        this.millis = millis;
    }
    
    public long getMillis() {
        return millis;
    } 
}
