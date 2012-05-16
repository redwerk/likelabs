package com.redwerk.likelabs.domain.model.query;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Pager {
    
    public static final Pager ALL_RECORDS = new Pager(0, Integer.MAX_VALUE);
    
    private final int offset;
    
    private final int limit;

    public Pager(int offset, int limit) {
        if (offset < 0 || limit < 0) {
            throw new IllegalArgumentException();
        }
        this.offset = offset;
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pager other = (Pager) obj;
        return new EqualsBuilder()
                .append(limit, other.limit)
                .append(offset, other.offset)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(limit)
                .append(offset)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("limit", limit)
                .append("offset", offset)
                .toString();
    }

}
