package com.redwerk.likelabs.application.dto;

public class SortingRule {

    private final SortingCriteria criteria;
    
    private final SortingOrder order;

    public SortingRule(SortingCriteria criteria, SortingOrder order) {
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
