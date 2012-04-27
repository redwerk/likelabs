package com.redwerk.likelabs.domain.model.tablet;

import com.redwerk.likelabs.domain.model.point.Point;

import java.util.List;

public interface TabletRepository {

    Tablet find(Long id);

    Tablet find(String login);

    List<Tablet> findAll(Point point);

    void add(Tablet tablet);

    void remove(Tablet tablet);

}
