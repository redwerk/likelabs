package com.redwerk.likelabs.infrastructure.persistence.jpa.review;

import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.query.SortingRule;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.review.ReviewContentType;
import com.redwerk.likelabs.domain.model.review.ReviewQuery;
import com.redwerk.likelabs.domain.model.review.ReviewStatus;
import com.redwerk.likelabs.domain.model.review.exception.ReviewNotFoundException;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.infrastructure.persistence.jpa.util.EntityJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewJpaQuery implements ReviewQuery {

    private static final String ORDER_BY = " order by r.createdDT desc, r.id";

    private static final String GET_REVIEWS_BY_AUTHOR =
            "select r from Review r where r.author.id = :authorId" + ORDER_BY;

    private static final String GET_REVIEWS_FOR_POINT =
            "select r from Review r where r.point.id = :pointId" + ORDER_BY;

    private static final String GET_REVIEWS_FOR_COMPANY =
            "select r from Review r where r.point.company.id = :companyId" + ORDER_BY;

    private EntityJpaRepository<Review, Long> entityRepository;


    public ReviewJpaQuery(EntityJpaRepository<Review, Long> entityRepository) {
        this.entityRepository = entityRepository;
    }

    /*
    @Override
    public List<Review> findAll(User author, int offset, int limit) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("authorId", author.getId());
        return getEntityRepository().findEntityList(GET_REVIEWS_BY_AUTHOR, parameters, offset, limit);
    }

    @Override
    public List<Review> findAll(Point point, int offset, int limit) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("pointId", point.getId());
        return getEntityRepository().findEntityList(GET_REVIEWS_FOR_POINT, parameters, offset, limit);
    }

    @Override
    public List<Review> findAll(Company company, int offset, int limit) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("companyId", company.getId());
        return getEntityRepository().findEntityList(GET_REVIEWS_FOR_COMPANY, parameters, offset, limit);
    }

    private EntityJpaRepository<Review, Long> getEntityRepository() {
        if (entityRepository == null) {
            entityRepository = new EntityJpaRepository<Review, Long>(em, Review.class);
        }
        return entityRepository;
    }
    */

    @Override
    public ReviewQuery setAuthorId(long authorId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ReviewQuery setModeratorId(long moderatorId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ReviewQuery setCompanyIds(List<Long> companyIds) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ReviewQuery setPointIds(List<Long> pointIds) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ReviewQuery setDateRange(Date fromDate, Date toDate) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ReviewQuery setContentType(ReviewContentType contentType) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ReviewQuery setStatus(ReviewStatus status) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ReviewQuery setPager(Pager pager) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ReviewQuery setSortingRule(SortingRule sortingRule) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Review> findReviews() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCount() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
