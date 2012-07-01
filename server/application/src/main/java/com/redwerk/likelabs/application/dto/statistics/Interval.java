package com.redwerk.likelabs.application.dto.statistics;

public enum Interval {

    MONTH_12(365*24*60*60L/30), MONTH_6(365*24*60*60L/60), DAYS_30(24*60*60L);

    private final Long sec;
    
    private Interval(Long sec) {
        this.sec=sec;
    }
    
    public Long getSec() {
        return this.sec;
    } 
}
