package com.redwerk.likelabs.domain.model.point;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address {

    @Column(table = "point_address")
    private String city;

    @Column(table = "point_address")
    private String state;

    @Column(table = "point_address")
    private String country;

    @Column(table = "point_address", name = "postal_code")
    private String postalCode;

    @Column(table = "point_address", name = "address_line_1")
    private String addressLine1;

    @Column(table = "point_address", name = "address_line_2")
    private String addressLine2;


    public Address(String city, String state, String country, String postalCode,
                   String addressLine1, String addressLine2) {
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    // overrides

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Address other = (Address) obj;
        return new EqualsBuilder()
                .append(city, other.city)
                .append(state, other.state)
                .append(country, other.country)
                .append(postalCode, other.postalCode)
                .append(addressLine1, other.addressLine1)
                .append(addressLine2, other.addressLine2)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(city)
                .append(state)
                .append(country)
                .append(postalCode)
                .append(addressLine1)
                .append(addressLine2)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("city", city)
                .append("state", state)
                .append("country", country)
                .append("postalCode", postalCode)
                .append("addressLine1", addressLine1)
                .append("addressLine2", addressLine2)
                .toString();
    }

    // interface for JPA

    protected Address() {
    }

}
