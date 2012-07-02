package com.redwerk.likelabs.infrastructure.persistence.jpa.review;

import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.review.ReviewQuery;
import com.redwerk.likelabs.domain.model.review.ReviewRepository;
import com.redwerk.likelabs.domain.model.review.exception.ReviewNotFoundException;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.infrastructure.persistence.jpa.util.EntityJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReviewJpaRepository extends ReviewRepository {

    private static final String GET_PHOTO_REVIEWS_QUERY =
            "select r from Review r where r.point.company.id = :companyId and r.photo is not null order by r.createdDT desc";

    @PersistenceContext
    private EntityManager em;

    private EntityJpaRepository<Review, Long> entityRepository;


    @Override
    public List<Review> findPhotoReviews(Company company) {
        return getEntityRepository().findEntityList(GET_PHOTO_REVIEWS_QUERY,
                Collections.<String, Object>singletonMap("companyId", company.getId()),
                Pager.ALL_RECORDS);
    }

    @Override
    public Review get(long id) {
        Review review = getEntityRepository().findById(id);
        if (review == null) {
            throw new ReviewNotFoundException(id);
        }
        return review;
    }

    @Override
    public ReviewQuery getQuery() {
        return new ReviewJpaQuery(getEntityRepository());
    }

    @Override
    public void add(Review review) {
        getEntityRepository().add(review);
    }

    @Override
    public void removeInternal(Review review) {
        getEntityRepository().remove(review);
    }
    
    private EntityJpaRepository<Review, Long> getEntityRepository() {
        if (entityRepository == null) {
            entityRepository = new EntityJpaRepository<Review, Long>(em, Review.class);
        }
        return entityRepository;
    }

}
