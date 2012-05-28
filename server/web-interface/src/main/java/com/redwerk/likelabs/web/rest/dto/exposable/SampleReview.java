package com.redwerk.likelabs.web.rest.dto.exposable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.web.rest.resource.mapping.LinkBuilder;


@XmlAccessorType(XmlAccessType.FIELD)
public class SampleReview {

    private final String author;

    private final String text;

    private final String imageUrl;


    public SampleReview(String author, String text, String imageUrl) {
        this.author = author;
        this.text = text;
        this.imageUrl = imageUrl;
    }

    /** Used internally by JAXB for object instantiation */
    @SuppressWarnings("unused")
    private SampleReview () {
        this(null, null, null);        
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
        .append("author", author)
        .append("text", text)
        .append("imageUrl", imageUrl)
        .toString();
    }

    
    public static class Builder {

        private LinkBuilder urlBuilder;
        
        private Review review;
        
        
        public Builder urlBuilder(LinkBuilder urlBuilder) {
            this.urlBuilder = urlBuilder;
            return this;
        }

        public Builder review(Review review) {
            this.review = review;
            return this;
        }

        public SampleReview build() {
            String imageUrl = null;
            if(review.getPhoto() != null && review.getPhoto().getImage() != null) {
                imageUrl = urlBuilder.reviewId(review.getId()).buildSampleReviewImageUrl();
            }           
            return new SampleReview(review.getAuthor().getName(), review.getMessage(), imageUrl);   
        }
    }

}
