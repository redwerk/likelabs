package com.redwerk.likelabs.application.dto.statistics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChartBuilder {
    
    private static final int POINTS_COUNT = 30;
    
    private List<ChartPoint> points;
    
    private final Interval interval;
    
    public ChartBuilder(Interval interval) {
        this.interval = interval;
        this.points = new ArrayList<ChartPoint>();
        Long start = new Date().getTime();
        for(int i = 0; i<POINTS_COUNT; i++){
            ChartPoint point = new ChartPoint();
            point.setDate(new Date(start-interval.getSec()*i));
            this.points.add(point);
        }
    }
    
    public void addPoint(ChartPoint point) {
        findPoint(point.getDate()).addPoint(point);
    }
    
    public List<ChartPoint> getPoints(){
        return this.points;
    }
    
    private ChartPoint findPoint(Date date) {
        for(ChartPoint point : this.points) {
            if(point.getDate().before(date)){
                return point;
            }
        }
        return null;
    }
    
}
