package com.redwerk.likelabs.application.dto;

import java.util.Date;
import java.util.List;

public class ReviewQuery {
    
    private final List<Long> pointIds;
    
    private final Date fromDate;
    
    private final Date toDate;
    
    private final ReviewType type;
    
    private final Pager pager;
    
    private final SortingRule sortingRule;

    public ReviewQuery(List<Long> pointIds, Date fromDate, Date toDate, ReviewType type, Pager pager, SortingRule sortingRule) {
        this.pointIds = pointIds;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.type = type;
        this.pager = pager;
        this.sortingRule = sortingRule;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Pager getPager() {
        return pager;
    }

    public List<Long> getPointIds() {
        return pointIds;
    }

    public SortingRule getSortingRule() {
        return sortingRule;
    }

    public Date getToDate() {
        return toDate;
    }

    public ReviewType getType() {
        return type;
    }
    
}
