package com.redwerk.likelabs.application;

import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.point.PointData;
import com.redwerk.likelabs.domain.model.point.Point;

import java.util.List;

public interface PointService {

    Report<Point> getPoints(long companyId, Pager pager);

    List<Point> getPoints(long companyId, long clientId);

    Point getPoint(long pointId);
    
    Point createPoint(long companyId, PointData pointData);

    void updatePoint(long pointId, PointData pointData);
    
    void deletePoint(long pointId);

}
