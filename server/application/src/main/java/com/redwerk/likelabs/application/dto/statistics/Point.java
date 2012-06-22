package com.redwerk.likelabs.application.dto.statistics;

import java.util.Date;

public class Point {

    private final Date date;

    private final int count;

    public Point(Date date, int count) {
        this.date = date;
        this.count = count;
    }

    public Date getDate() {
        return date;
    }

    public int getCount() {
        return count;
    }

}
