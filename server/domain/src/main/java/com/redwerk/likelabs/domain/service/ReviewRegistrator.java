package com.redwerk.likelabs.domain.service;

import com.redwerk.likelabs.domain.service.dto.PhotoData;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.tablet.Tablet;
import com.redwerk.likelabs.domain.service.dto.RecipientData;

import java.util.List;

public interface ReviewRegistrator {
    
    Review registerReview(Tablet tablet, String authorPhone, String text, List<PhotoData> photos,
                          List<RecipientData> recipients);

}
