package com.redwerk.likelabs.web.rest.resource;

import javax.ws.rs.Path;

import com.redwerk.likelabs.application.ReviewService;
import com.redwerk.likelabs.domain.model.photo.Photo;


public class ReviewResource {
      
    private final ReviewService reviewService;
    
    private final long reviewId;
    
    
    public ReviewResource(long reviewId, ReviewService reviewService) {
        this.reviewId = reviewId;
        this.reviewService = reviewService;
    }
    
    @Path("image")
    public ImageResource getImage() {
        Photo photo = reviewService.getReview(reviewId).getPhoto();
        return photo == null ? null : new ImageResource(photo.getImage());
    }
}
