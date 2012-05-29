package com.redwerk.likelabs.domain.model.tablet;

import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.query.Pager;

import java.util.List;

public interface TabletRepository {

    Tablet get(long id);

    Tablet get(String apiKey);

    Tablet find(String login);

    List<Tablet> findAll(Point point, Pager pager);
    
    int getCount(Point point);

    void add(Tablet tablet);

    void remove(Tablet tablet);

}
