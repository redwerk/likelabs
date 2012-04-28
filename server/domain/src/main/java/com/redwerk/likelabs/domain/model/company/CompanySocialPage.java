package com.redwerk.likelabs.domain.model.company;

import com.redwerk.likelabs.domain.model.SocialNetworkType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Embeddable;

@Embeddable
public class CompanySocialPage implements Comparable<CompanySocialPage> {

    private SocialNetworkType type;

    private String pageId;


    // constructors

    public CompanySocialPage(SocialNetworkType type, String pageId) {
        this.type = type;
        this.pageId = pageId;
    }

    // accessors

    public SocialNetworkType getType() {
        return type;
    }

    public String getPageId() {
        return pageId;
    }

    // implementation of Comparable<CompanySocialPage>

    @Override
    public int compareTo(CompanySocialPage other) {
        int res = type.compareTo(other.type);
        return (res != 0) ? res: pageId.compareTo(other.pageId);
    }

    // overrides

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CompanySocialPage other = (CompanySocialPage) obj;
        return new EqualsBuilder()
                .append(type, other.type)
                .append(pageId, other.pageId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(type)
                .append(pageId)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("type", type)
                .append("pageId", pageId)
                .toString();
    }

    // interface for JPA

    protected CompanySocialPage() {
    }

}
