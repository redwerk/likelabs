package com.redwerk.likelabs.infrastructure.persistence.jpa;

import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.review.ReviewRepository;
import com.redwerk.likelabs.domain.model.review.exception.ReviewNotFoundException;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.infrastructure.persistence.jpa.util.EntityJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReviewJpaRepository implements ReviewRepository {

    private static final String ORDER_BY = " order by r.createdDT desc, r.id";

    private static final String GET_REVIEWS_BY_AUTHOR =
            "select r from Review r where r.author.id = :authorId" + ORDER_BY;

    private static final String GET_REVIEWS_FOR_POINT =
            "select r from Review r where r.point.id = :pointId" + ORDER_BY;

    private static final String GET_REVIEWS_FOR_COMPANY =
            "select r from Review r where r.point.company.id = :companyId" + ORDER_BY;

    @PersistenceContext
    private EntityManager em;

    private EntityJpaRepository<Review, Long> entityRepository;

    @Override
    public Review get(long id) {
        Review review = getEntityRepository().findById(id);
        if (review == null) {
            throw new ReviewNotFoundException(id);
        }
        return review;
    }

    @Override
    public List<Review> findAll(User author, int offset, int count) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("authorId", author.getId());
        return getEntityRepository().findEntityList(GET_REVIEWS_BY_AUTHOR, parameters, offset, count);
    }

    @Override
    public List<Review> findAll(Point point, int offset, int count) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("pointId", point.getId());
        return getEntityRepository().findEntityList(GET_REVIEWS_FOR_POINT, parameters, offset, count);
    }

    @Override
    public List<Review> findAll(Company company, int offset, int count) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("companyId", company.getId());
        return getEntityRepository().findEntityList(GET_REVIEWS_FOR_COMPANY, parameters, offset, count);
    }

    @Override
    public void add(Review review) {
        getEntityRepository().add(review);
    }

    @Override
    public void remove(Review review) {
        getEntityRepository().remove(review);
    }

    private EntityJpaRepository<Review, Long> getEntityRepository() {
        if (entityRepository == null) {
            entityRepository = new EntityJpaRepository<Review, Long>(em, Review.class);
        }
        return entityRepository;
    }
}
