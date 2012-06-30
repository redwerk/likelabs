package com.redwerk.likelabs.domain.service.impl;

import com.redwerk.likelabs.domain.model.company.CompanyRepository;
import com.redwerk.likelabs.domain.model.event.EventRepository;
import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.model.photo.PhotoRepository;
import com.redwerk.likelabs.domain.model.photo.PhotoStatus;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.review.RecipientFactory;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.review.ReviewRepository;
import com.redwerk.likelabs.domain.model.tablet.Tablet;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserRepository;
import com.redwerk.likelabs.domain.service.RecipientNotifier;
import com.redwerk.likelabs.domain.service.ReviewRegistrator;
import com.redwerk.likelabs.domain.service.UserRegistrator;
import com.redwerk.likelabs.domain.service.dto.PhotoData;
import com.redwerk.likelabs.domain.service.dto.RecipientData;
import com.redwerk.likelabs.domain.service.exception.ReviewCreatedByAdminException;
import com.redwerk.likelabs.domain.service.notification.NotificationProcessor;
import com.redwerk.likelabs.domain.service.sn.GatewayFactory;
import com.redwerk.likelabs.domain.service.sn.ImageSourceFactory;
import org.apache.commons.lang.Validate;

import java.util.List;

public class BasicReviewRegistrator implements ReviewRegistrator {

    private final UserRepository userRepository;

    private final UserRegistrator userRegistrator;

    private final CompanyRepository companyRepository;

    private final ReviewRepository reviewRepository;

    private final PhotoRepository photoRepository;

    private final EventRepository eventRepository;

    private final GatewayFactory gatewayFactory;

    private final ImageSourceFactory imageSourceFactory;

    private final RecipientNotifier recipientNotifier;

    private final NotificationProcessor notificationProcessor;


    public BasicReviewRegistrator(UserRepository userRepository, UserRegistrator userRegistrator,
                                  CompanyRepository companyRepository, ReviewRepository reviewRepository,
                                  PhotoRepository photoRepository, EventRepository eventRepository,
                                  GatewayFactory gatewayFactory, ImageSourceFactory imageSourceFactory,
                                  RecipientNotifier recipientNotifier, NotificationProcessor notificationProcessor) {
        this.userRepository = userRepository;
        this.userRegistrator = userRegistrator;
        this.companyRepository = companyRepository;
        this.reviewRepository = reviewRepository;
        this.photoRepository = photoRepository;
        this.eventRepository = eventRepository;
        this.gatewayFactory = gatewayFactory;
        this.imageSourceFactory = imageSourceFactory;
        this.recipientNotifier = recipientNotifier;
        this.notificationProcessor = notificationProcessor;
    }

    @Override
    public Review registerReview(Tablet tablet, String authorPhone, String text, List<PhotoData> photos,
                                 List<RecipientData> recipients) {
        Validate.notNull(tablet, "tablet is required for review creation");
        Validate.notEmpty(authorPhone, "authorPhone is required for review creation");
        Validate.notNull(photos, "photos cannot be null");
        Validate.notNull(recipients, "recipients cannot be null");

        User author = getUser(authorPhone);
        Photo reviewPhoto = processPhotos(author, photos);

        Review review = createReview(author, tablet.getPoint(), text, reviewPhoto);

        //notifyAuthor(author, review);
        notifyClients(tablet.getPoint(), review);
        notifyRecipients(review, recipients);

        return review;
    }

    private User getUser(String phone) {
        User user = userRepository.find(phone);
        if (user == null) {
            user = userRegistrator.registerUser(phone);
        }
        else if (!companyRepository.findForAdmin(user, Pager.ALL_RECORDS).isEmpty()) {
            throw new ReviewCreatedByAdminException(user);
        }
        return user;
    }

    private Photo processPhotos(User author, List<PhotoData> photos) {
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
        if (!photos.isEmpty() && selectedPhoto == null) {
            throw new IllegalArgumentException("review photo is not selected");
        }
        return selectedPhoto;
    }

    private Review createReview(User author, Point point, String text, Photo photo) {
        Review review = Review.createReview(author, point, text, photo);
        reviewRepository.add(review);
        return review;
    }
   /*
    private void notifyAuthor(User author, Review review) {
        author.registerOwnReview(review, eventRepository, notificationProcessor, gatewayFactory, imageSourceFactory);
    }
*/
    private void notifyClients(Point point, Review review) {
        User author = review.getAuthor();
        for (User client: userRepository.findClients(point)) {
            if (!client.equals(author)) {
                client.registerClientReview(review, eventRepository, notificationProcessor);
            }
        }
    }

    private void notifyRecipients(Review review, List<RecipientData> recipients) {
        RecipientFactory recipientFactory = new RecipientFactory();
        for (RecipientData recipientData: recipients) {
            review.addRecipient(
                    recipientFactory.createReviewRecipient(review, recipientData.getType(), recipientData.getAddress()));
        }
        review.notifyRecipients(recipientNotifier);
    }

}
