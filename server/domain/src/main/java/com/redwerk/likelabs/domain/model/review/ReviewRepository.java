package com.redwerk.likelabs.domain.model.review;

import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.user.User;

import java.util.List;

public interface ReviewRepository {

    ReviewQuery getQuery();

    Review get(long id);

    void add(Review review);

    void remove(Review review);

}
