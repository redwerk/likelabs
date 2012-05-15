package com.redwerk.likelabs.domain.model.point.exception;

public class PointNotFoundException extends RuntimeException {

    private final long pointId;

    public PointNotFoundException(long pointId) {
        this.pointId = pointId;
    }

    public long getPointId() {
        return pointId;
    }

}
