package com.redwerk.likelabs.domain.model.review;

import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.user.User;

import java.util.List;

public interface ReviewRepository {

    Review find(Long id);

    List<Review> findAll(User author);
    
    List<Review> findAll(Point point);

    List<Review> findAll(Company company);

    void add(Review review);

    void remove(Review review);

}
