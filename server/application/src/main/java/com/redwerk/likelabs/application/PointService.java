package com.redwerk.likelabs.application;

import com.redwerk.likelabs.application.dto.Pager;
import com.redwerk.likelabs.application.dto.PointData;
import com.redwerk.likelabs.domain.model.point.Point;

import java.util.List;

public interface PointService {

    List<Point> getPoints(long companyId, Pager pager);
    
    Point getPoint(long pointId);
    
    Point createPoint(long companyId, PointData pointData);

    void updatePoint(long pointId, PointData pointData);
    
    void deletePoint(long pointId);

}
