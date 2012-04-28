package com.redwerk.likelabs.domain.model.point;

import com.redwerk.likelabs.domain.model.company.Company;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Embedded
    private Address address;

    private String phone;


    // constructors
    
    public Point(Company company,  Address address, String phone) {
        this.company = company;
        this.address = address;
        this.phone = phone;
    }

    // accessors

    public Long getId() {
        return id;
    }

    public Company getCompany() {
        return company;
    }

    public Address getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    // modifiers

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // overrides

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point other = (Point) obj;
        return new EqualsBuilder()
                .append(company, other.company)
                .append(address, other.address)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(company)
                .append(address)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("company", company)
                .append("address", address)
                .append("phone", phone)
                .toString();
    }

    // interface for JPA

    protected Point() {
    }

}
