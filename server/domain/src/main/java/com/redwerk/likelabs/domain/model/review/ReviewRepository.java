package com.redwerk.likelabs.domain.model.review;

import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.user.User;

import java.util.List;

public abstract class ReviewRepository {

    public abstract ReviewQuery getQuery();

    public abstract Review get(long id);

    public abstract void add(Review review);

    public abstract void remove(Review review);

}
