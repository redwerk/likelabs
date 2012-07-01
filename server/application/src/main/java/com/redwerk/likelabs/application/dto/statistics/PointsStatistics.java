package com.redwerk.likelabs.application.dto.statistics;

import java.util.List;

public class PointsStatistics {

    private final ParameterType type;

    private final List<Point> points;
    
    public PointsStatistics(ParameterType type, List<Point> points) {
        this.type = type;
        this.points = points;
    }

    public ParameterType getType() {
        return type;
    }

    public List<Point> getPoints() {
        return points;
    }

}
