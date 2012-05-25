package com.redwerk.likelabs.domain.model.review;

import static com.redwerk.likelabs.domain.model.review.RecipientType.*;

public class RecipientFactory {
    
    public Recipient createReviewRecipient(Review review, RecipientType type, String destination) {
        switch (type) {
            case SMS:
                return new SmsRecipient(review, destination);
            case EMAIL:
                return new EmailRecipient(review, destination);
            default:
                throw new IllegalArgumentException("unexpected review type " + type);
        }
    }
    
}
