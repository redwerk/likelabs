package com.redwerk.likelabs.domain.service.impl;

import com.redwerk.likelabs.domain.model.event.EventRepository;
import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.model.photo.PhotoRepository;
import com.redwerk.likelabs.domain.model.photo.PhotoStatus;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.review.RecipientFactory;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.review.ReviewRepository;
import com.redwerk.likelabs.domain.model.review.ReviewStatus;
import com.redwerk.likelabs.domain.model.tablet.Tablet;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserRepository;
import com.redwerk.likelabs.domain.service.RecipientNotifier;
import com.redwerk.likelabs.domain.service.ReviewRegistrator;
import com.redwerk.likelabs.domain.service.UserRegistrator;
import com.redwerk.likelabs.domain.service.dto.PhotoData;
import com.redwerk.likelabs.domain.service.dto.RecipientData;
import com.redwerk.likelabs.domain.service.sn.GatewayFactory;
import com.redwerk.likelabs.domain.service.sn.ImageSourceFactory;
import org.apache.commons.lang.Validate;

import java.util.List;

public class BasicReviewRegistrator implements ReviewRegistrator {

    private final UserRepository userRepository;

    private final UserRegistrator userRegistrator;

    private final ReviewRepository reviewRepository;

    private final PhotoRepository photoRepository;

    private final EventRepository eventRepository;

    private final GatewayFactory gatewayFactory;

    private final ImageSourceFactory imageSourceFactory;

    private final RecipientNotifier recipientNotifier;


    public BasicReviewRegistrator(UserRepository userRepository, UserRegistrator userRegistrator, ReviewRepository reviewRepository,
                                  PhotoRepository photoRepository, EventRepository eventRepository, GatewayFactory gatewayFactory,
                                  ImageSourceFactory imageSourceFactory, RecipientNotifier recipientNotifier) {
        this.userRepository = userRepository;
        this.userRegistrator = userRegistrator;
        this.reviewRepository = reviewRepository;
        this.photoRepository = photoRepository;
        this.eventRepository = eventRepository;
        this.gatewayFactory = gatewayFactory;
        this.imageSourceFactory = imageSourceFactory;
        this.recipientNotifier = recipientNotifier;
    }

    @Override
    public Review createAndRegisterReview(Tablet tablet, String authorPhone, String text, List<PhotoData> photos,
                                          List<RecipientData> recipients) {
        Validate.notNull(tablet, "tablet is required for review creation");
        Validate.notEmpty(authorPhone, "authorPhone is required for review creation");
        Validate.notNull(photos, "photos cannot be null");
        Validate.notNull(recipients, "recipients cannot be null");

        User author = getOrCreateUser(authorPhone);
        Photo reviewPhoto = photos.isEmpty() ? null : processPhotosAndGetSelected(author, photos);
        Review review = Review.createReview(author, tablet.getPoint(), text, reviewPhoto);
        processRecipients(review, recipients);
        reviewRepository.add(review);
        // review.notifyRecipients(recipientNotifier);
        author.registerReview(review, eventRepository, gatewayFactory, imageSourceFactory);
        return review;
    }

    private Photo processPhotosAndGetSelected(User author, List<PhotoData> photos) {
        Photo selectedPhoto = null;
        for (PhotoData photoData: photos) {
            Photo photo = new Photo(author, photoData.getStatus(), photoData.getImage());
            if (photo.getStatus() == PhotoStatus.SELECTED) {
                if (selectedPhoto != null) {
                    throw new IllegalArgumentException("only one photo can be selected");
                }
                selectedPhoto = photo;
            }
            photoRepository.add(photo);
        }
        if (selectedPhoto == null) {
            throw new IllegalArgumentException("review photo is not selected");
        }
        return selectedPhoto;
    }

    private void processRecipients(Review review, List<RecipientData> recipients) {
        RecipientFactory recipientFactory = new RecipientFactory();
        for (RecipientData recipientData: recipients) {
            review.addRecipient(
                    recipientFactory.createReviewRecipient(review, recipientData.getType(), recipientData.getAddress()));
        }
    }

    private User getOrCreateUser(String phone) {
        User user = userRepository.find(phone);
        if (user == null) {
            user = userRegistrator.registerUser(phone);
        }
        return user;
    }

}
