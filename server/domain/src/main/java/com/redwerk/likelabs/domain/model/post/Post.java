package com.redwerk.likelabs.domain.model.post;

import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.user.User;

import java.util.Date;

public abstract class Post {

    private Long id;

    private Review review;

    private User recipient;

    private Date created;

    public Long getId() {
        return id;
    }

    public Review getReview() {
        return review;
    }

    public User getRecipient() {
        return recipient;
    }

    public Date getCreated() {
        return created;
    }

}
