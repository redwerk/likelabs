package com.redwerk.likelabs.web.rest.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import com.google.common.base.CharMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redwerk.likelabs.application.ReviewService;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.web.rest.dto.exposable.ReviewPrototype;
import com.redwerk.likelabs.web.rest.dto.utils.DataConverter;
import com.redwerk.likelabs.web.rest.resource.mapping.LinkBuilder;

public class ReviewsResource {
    
    private static final Logger LOG = LoggerFactory.getLogger(ReviewsResource.class);
    
    private final ReviewService reviewService;
    
    private final long tabletId;

    
    public ReviewsResource(long tabletId, ReviewService reviewService) {
        this.tabletId =  tabletId;
        this.reviewService = reviewService;
    }
    
    @POST 
    @Consumes(MediaType.APPLICATION_XML)
    public void addReview(ReviewPrototype reviewProto) {
        LOG.info("got new review to create: {}", reviewProto);
        Review review = reviewService.createReview(tabletId, getNormalizedPhone(reviewProto.getAuthorPhone()),
                reviewProto.getText(),
                DataConverter.convertPhotos(reviewProto.getPhotos()), 
                DataConverter.convertRecipients(reviewProto.getRecipients()));
        if (review == null) {
            LOG.error("review creating failed, reviewProto: {}", reviewProto);
            throw new WebApplicationException();
        }
    }
    
    private String getNormalizedPhone(String phone) {
        return "+" + CharMatcher.DIGIT.retainFrom(phone);
    }
    
    @Path("{reviewId}")
    public ReviewResource getReview(@PathParam(LinkBuilder.Param.REVIEW_ID) long reviewId) {
        return new ReviewResource(reviewId, reviewService);
    }
}
