package com.redwerk.likelabs.domain.model.point;

import com.redwerk.likelabs.domain.model.company.Company;

import java.util.List;

public interface PointRepository {
    
    Point get(long id);

    Point find(Company company, Address address);

    List<Point> findAll(Company company, int offset, int count);

    void add(Point point);

    void remove(Point point);

}
