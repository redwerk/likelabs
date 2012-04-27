package com.redwerk.likelabs.domain.model.point;

import com.redwerk.likelabs.domain.model.company.Company;

import java.util.List;

public interface PointRepository {
    
    Point find(Long id);

    Point find(Company company, Address address);

    List<Point> findAll(Company company);

    void add(Point point);

    void remove(Point point);

}
