package com.redwerk.likelabs.application.impl.point;

import com.redwerk.likelabs.application.dto.point.PointData;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.point.PointRepository;

public class PointFactory {

    private final PointRepository repository;

    public PointFactory(PointRepository repository) {
        this.repository = repository;
    }

    public Point createPoint(Company company, PointData pointData) {
        Point point = new Point(company, pointData.getAddress(), pointData.getPhone(), pointData.getEmail());
        repository.add(point);
        return point;
    }

}
