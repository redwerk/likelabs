package com.redwerk.likelabs.domain.model.review;

import com.redwerk.likelabs.domain.model.review.SortingCriteria;
import com.redwerk.likelabs.domain.model.review.SortingOrder;

public class SortingRule {

    private final SortingCriteria criteria;
    
    private final SortingOrder order;

    public SortingRule(SortingCriteria criteria, SortingOrder order) {
        if (criteria == null || order == null) {
            throw new IllegalArgumentException("Sorting criteria and order cannot be null");
        }
        this.criteria = criteria;
        this.order = order;
    }

    public SortingCriteria getCriteria() {
        return criteria;
    }

    public SortingOrder getOrder() {
        return order;
    }
    
}
