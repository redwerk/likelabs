package com.redwerk.likelabs.domain.service;

import com.redwerk.likelabs.domain.model.review.Review;

public interface RecipientNotifier {
    
    boolean notifyBySms(String phone, Review review);

    boolean notifyByEmail(String email, Review review);

}
