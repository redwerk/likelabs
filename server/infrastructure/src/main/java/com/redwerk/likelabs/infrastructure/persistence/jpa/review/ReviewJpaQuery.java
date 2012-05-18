package com.redwerk.likelabs.infrastructure.persistence.jpa.review;

import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.review.SortingCriteria;
import com.redwerk.likelabs.domain.model.review.SortingOrder;
import com.redwerk.likelabs.domain.model.review.SortingRule;
import com.redwerk.likelabs.domain.model.review.*;
import com.redwerk.likelabs.infrastructure.persistence.jpa.util.EntityJpaRepository;
import org.apache.commons.lang.time.DateUtils;

import java.text.MessageFormat;
import java.util.*;

public class ReviewJpaQuery implements ReviewQuery {

    private static final SortingRule DEFAULT_SORTING_RULE =
            new SortingRule(SortingCriteria.DATE, SortingOrder.DESCENDING);

    private static final Map<ContentTypeFilter, String> TYPE_SUB_FILTERS = new HashMap<ContentTypeFilter, String>() {{
        put(ContentTypeFilter.CONTAINS_PHOTO, "r.photo is not null");
        put(ContentTypeFilter.CONTAINS_TEXT, "r.message is not null");
        put(ContentTypeFilter.CONTAINS_TEXT_AND_PHOTO, "r.message is not null and r.photo is not null");
    }};

     private static final Map<SortingCriteria, String> ORDER_BY_EXPRESSIONS = new HashMap<SortingCriteria, String>() {{
        put(SortingCriteria.DATE, "r.createdDT {0}");
        put(SortingCriteria.POINT,
                "r.point.company.name {0}, a.country {0}, a.state {0}, a.city {0}, a.addressLine1 {0}, a.addressLine2 {0}");
        put(SortingCriteria.REVIEW_STATUS, "r.status {0}");
        put(SortingCriteria.REVIEW_TYPE, "r.photo {0}, r.message {0}");
    }};

    private static final Map<SortingOrder, String> SORTING_ORDER_EXPRESSIONS = new HashMap<SortingOrder, String>() {{
        put(SortingOrder.ASCENDING, "asc");
        put(SortingOrder.DESCENDING, "desc");
    }};

    private static final String REVIEWS_FILTER =
            "from Review r join r.point.addresses a where ((:authorId is null or r.author.id = :authorId) and " +
                    "(:moderatorId is null or r.moderator.id = :moderatorId) and " +
                    "(:companyIds is null or r.point.company.id in :companyIds) and " +
                    "(:pointIds is null or r.point.id in :pointIds) and " +
                    "(:fromDate is null or r.createdDT >= :fromDate) and " +
                    "(:toDate is null or r.createdDT <= :toDate) and " +
                    "(:status is null or r.status = :status))";

    private static final String REVIEWS_QUERY = "select r {0} {1}";

    private static final String REVIEWS_COUNT_QUERY = "select count(r) {0}";


    private EntityJpaRepository<Review, Long> entityRepository;

    private Long authorId;

    private Long moderatorId;

    private List<Long> companyIds;

    private List<Long> pointIds;

    private Date fromDate;

    private Date toDate;

    private ContentTypeFilter contentType;

    private ReviewStatus status;

    private Pager pager;

    private SortingRule sortingRule;


    public ReviewJpaQuery(EntityJpaRepository<Review, Long> entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public ReviewQuery setAuthorId(Long authorId) {
        this.authorId = authorId;
        return this;
    }

    @Override
    public ReviewQuery setModeratorId(Long moderatorId) {
        this.moderatorId = moderatorId;
        return this;
    }

    @Override
    public ReviewQuery setCompanyIds(List<Long> companyIds) {
        this.companyIds =
                (companyIds != null && companyIds.isEmpty()) ? null : companyIds;
        return this;
    }

    @Override
    public ReviewQuery setPointIds(List<Long> pointIds) {
        this.pointIds =
                (pointIds != null && companyIds.isEmpty()) ? null : pointIds;
        return this;
    }

    @Override
    public ReviewQuery setDateRange(Date fromDate, Date toDate) {
        this.fromDate = (fromDate != null) ? DateUtils.truncate(fromDate, Calendar.DATE) : null;
        this.toDate = (toDate != null) ? DateUtils.truncate(toDate, Calendar.DATE) : null;
        return this;
    }

    @Override
    public ReviewQuery setContentType(ContentTypeFilter contentType) {
        this.contentType = contentType;
        return this;
    }

    @Override
    public ReviewQuery setStatus(ReviewStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public ReviewQuery setPager(Pager pager) {
        this.pager = pager;
        return this;
    }

    @Override
    public ReviewQuery setSortingRule(SortingRule sortingRule) {
        this.sortingRule = sortingRule;
        return this;
    }

    @Override
    public List<Review> findReviews() {
        String query = MessageFormat.format(REVIEWS_QUERY, getQueryString(), getOrderByStatement());
        return entityRepository.findEntityList(query, getParameters(), (pager != null) ? pager : Pager.ALL_RECORDS);
    }

    @Override
    public int getCount() {
        String query = MessageFormat.format(REVIEWS_COUNT_QUERY, getQueryString());
        return entityRepository.getCount(query, getParameters());
    }
    
    private Map<String, Object> getParameters() {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("authorId", authorId);
        parameters.put("moderatorId", moderatorId);
        parameters.put("companyIds", companyIds);
        parameters.put("pointIds", pointIds);
        parameters.put("fromDate", fromDate);
        parameters.put("toDate", toDate);
        parameters.put("status", status);
        return parameters;
    }
    
    private String getQueryString() {
        return (contentType == null) ?
                REVIEWS_FILTER :
                MessageFormat.format("{0} and ({1})", REVIEWS_FILTER, TYPE_SUB_FILTERS.get(contentType));
    }

    private String getOrderByStatement() {
        if (sortingRule == null) {
            sortingRule = DEFAULT_SORTING_RULE;
        }
        StringBuilder result = new StringBuilder("order by ");
        result.append(getOrderBySubExpression(sortingRule));
        if (sortingRule.getCriteria() != SortingCriteria.DATE) {
            result.append(", ").append(getOrderBySubExpression(DEFAULT_SORTING_RULE));
        }
        result.append(", r.id desc");
        return result.toString();
    }

    private String getOrderBySubExpression(SortingRule subRule) {
        return MessageFormat.format(
                ORDER_BY_EXPRESSIONS.get(subRule.getCriteria()),
                SORTING_ORDER_EXPRESSIONS.get(subRule.getOrder())
        );
    }

}
