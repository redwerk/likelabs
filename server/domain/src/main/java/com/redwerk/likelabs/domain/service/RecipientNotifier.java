package com.redwerk.likelabs.domain.service;

public interface RecipientNotifier {
    
    void notifyBySms(String email, long reviewId, String authorName);

    void notifyByEmail(String email, long reviewId, String authorName);

}
