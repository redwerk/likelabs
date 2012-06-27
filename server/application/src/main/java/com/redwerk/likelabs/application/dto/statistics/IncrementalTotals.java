package com.redwerk.likelabs.application.dto.statistics;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class IncrementalTotals {

    private int all;
    
    private int last24Hours;

    public int getAll() {
        return all;
    }

    public int getLast24Hours() {
        return last24Hours;
    }

    public int getLastMonth() {
        return lastMonth;
    }

    public int getLastWeek() {
        return lastWeek;
    }
    
    private int lastWeek;
    
    private int lastMonth;
    
    private Calendar c24 = new GregorianCalendar();
    
    private Calendar cWeek = new GregorianCalendar();
    
    private Calendar cMonth = new GregorianCalendar();
    
    public IncrementalTotals() {
        c24.add(Calendar.DATE, -1);
        cWeek.add(Calendar.DATE, -7);
        cMonth.add(Calendar.MONTH, -1);
    }
    
    public void increment(int count, Calendar date) {
        
        if (c24.before(date)) {
            last24Hours += count;
        }
        if (cWeek.before(date)) {
            lastWeek += count;
        }
        if (cMonth.before(date)) {
            lastMonth += count;
        }
        all += count;
        
    }
    
    public Totals getTotals() {
        return new Totals(all, last24Hours, lastWeek, lastMonth);
    }
    
}