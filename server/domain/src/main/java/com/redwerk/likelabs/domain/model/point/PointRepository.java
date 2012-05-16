package com.redwerk.likelabs.domain.model.point;

import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.query.Pager;

import java.util.List;

public interface PointRepository {
    
    Point get(long id);

    Point find(Company company, Address address);

    List<Point> findAll(Company company, Pager pager);

    int getCount(Company company);

    void add(Point point);

    void remove(Point point);

}
