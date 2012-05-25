package com.redwerk.likelabs.domain.service;

public interface RecipientNotifier {
    
    boolean notifyBySms(String email, long reviewId, String authorName);

    boolean notifyByEmail(String email, long reviewId, String authorName);

}
