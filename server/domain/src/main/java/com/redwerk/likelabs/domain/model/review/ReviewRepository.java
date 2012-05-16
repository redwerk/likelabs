package com.redwerk.likelabs.domain.model.review;

import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.user.User;

import java.util.List;

public interface ReviewRepository {

    Review get(long id);

    List<Review> findAll(User author, int offset, int limit);
    
    List<Review> findAll(Point point, int offset, int limit);

    List<Review> findAll(Company company, int offset, int limit);
    
    int getCount();

    void add(Review review);

    void remove(Review review);

}
