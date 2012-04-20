package com.redwerk.likelabs.application;

import com.redwerk.likelabs.application.dto.PointData;
import com.redwerk.likelabs.domain.model.Point;

import java.util.List;

public interface PointService {

    Point getPoint(long pointId);

    List<Point> getPoints(long companyId);

    Point createPoint(long companyId, PointData companyData);

    void deletePoint(long pointId);

    void updatePoint(long pointId, PointData pointData);

}
