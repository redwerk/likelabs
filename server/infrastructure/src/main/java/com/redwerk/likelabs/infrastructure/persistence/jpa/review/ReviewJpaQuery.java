package com.redwerk.likelabs.infrastructure.persistence.jpa.review;

import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.review.SortingCriteria;
import com.redwerk.likelabs.domain.model.review.SortingOrder;
import com.redwerk.likelabs.domain.model.review.SortingRule;
import com.redwerk.likelabs.domain.model.review.*;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.infrastructure.persistence.jpa.util.EntityJpaRepository;

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
        put(SortingCriteria.COMPANY_AND_POINT,
                "r.point.company.name {0}, r.point.address.country {0}, r.point.address.state {0}, r.point.address.city {0}, r.point.address.addressLine1 {0}, r.point.address.addressLine2 {0}");
        put(SortingCriteria.REVIEW_STATUS, "r.status {0}");
        put(SortingCriteria.REVIEW_TYPE, "isnull(r.photo), isnull(r.message)");
    }};

    private static final Map<SortingOrder, String> SORTING_ORDER_EXPRESSIONS = new HashMap<SortingOrder, String>() {{
        put(SortingOrder.ASCENDING, "asc");
        put(SortingOrder.DESCENDING, "desc");
    }};

    private static final String REVIEWS_FILTER =
            "from Review r where ((:authorId is null or r.author.id = :authorId) and " +
                    "(:admin is null or :admin in elements(r.point.company.admins)) and " +
                    "(:filterByCompanies = false or r.point.company.id in (:companyIds)) and " +
                    "(:filterByPoints = false or r.point.id in (:pointIds)) and " +
                    "(:fromDate is null or r.createdDT >= :fromDate) and " +
                    "(:toDate is null or r.createdDT < :toDate) and " +
                    "(:status is null or r.status = :status) and " +
                    "(:sampleStatus is null or " +
                        "(:sampleStatus = true and r in elements(r.point.company.sampleReviews)) or " +
                        "(:sampleStatus = false and r not in elements(r.point.company.sampleReviews))) and " +
                    "(:publishingStatus is null or r.publishedInCompanySN = :publishingStatus))";

    private static final String REVIEWS_QUERY = "select r {0} {1}";

    private static final String REVIEWS_COUNT_QUERY = "select count(r) {0}";


    private EntityJpaRepository<Review, Long> entityRepository;

    private Long authorId;

    private User admin;

    private boolean filterByCompanies = false;

    private List<Long> companyIds;

    private boolean filterByPoints = false;

    private List<Long> pointIds;

    private Date fromDate;

    private Date toDate;

    private ContentTypeFilter contentType;

    private ReviewStatus status;
    
    private Boolean sampleStatus;

    private Boolean publishingStatus;

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
    public ReviewQuery setAdmin(User admin) {
        this.admin = admin;
        return this;
    }

    @Override
    public ReviewQuery setCompanyIds(List<Long> companyIds) {
        this.companyIds = (companyIds != null && companyIds.isEmpty()) ? null : companyIds;
        this.filterByCompanies = (this.companyIds != null);
        return this;
    }

    @Override
    public ReviewQuery setPointIds(List<Long> pointIds) {
        this.pointIds = (pointIds != null && pointIds.isEmpty()) ? null : pointIds;
        this.filterByPoints = (this.pointIds != null);
        return this;
    }

    @Override
    public ReviewQuery setDateRange(Date fromDate, Date toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
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
    public ReviewQuery setSampleStatus(Boolean sampleStatus) {
        this.sampleStatus = sampleStatus;
        return this;
    }

    @Override
    public ReviewQuery setPublishingStatus(Boolean publishingStatus) {
        this.publishingStatus = publishingStatus;
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
        parameters.put("admin", admin);
        parameters.put("companyIds", companyIds);
        parameters.put("filterByCompanies", filterByCompanies);
        parameters.put("pointIds", pointIds);
        parameters.put("filterByPoints", filterByPoints);
        parameters.put("fromDate", fromDate);
        parameters.put("toDate", toDate);
        parameters.put("status", (status != null) ? status.ordinal() : null);
        parameters.put("sampleStatus", sampleStatus);
        parameters.put("publishingStatus", publishingStatus);
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
