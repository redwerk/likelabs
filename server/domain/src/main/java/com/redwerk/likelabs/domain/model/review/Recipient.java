package com.redwerk.likelabs.domain.model.review;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Embeddable;

@Embeddable
public class Recipient {

    private RecipientType type;
    
    private String address;

    public Recipient(RecipientType type, String address) {
        this.type = type;
        this.address = address;
    }

    public RecipientType getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Recipient other = (Recipient) obj;
        return new EqualsBuilder()
                .append(type, other.type)
                .append(address, other.address)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(type)
                .append(address)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("type", type)
                .append("address", address)
                .toString();
    }

    // interface for JPA

    protected Recipient() {
    }

}
