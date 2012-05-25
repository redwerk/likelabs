package com.redwerk.likelabs.domain.model.point;

import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.review.ReviewRegistrationAgent;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
@Table(name = "point")
@SecondaryTable(name = "point_address", pkJoinColumns = @PrimaryKeyJoinColumn(name = "point_id", referencedColumnName = "id"))
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

    private String email;
    

    // constructors
    
    public Point(Company company, Address address, String phone, String email) {
        this.company = company;
        this.address = address;
        this.phone = phone;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    // modifiers

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // reviews

    public void registerReview(Review review, ReviewRegistrationAgent registrationAgent) {
        company.registerReview(review, registrationAgent);
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
                .append("email", email)
                .toString();
    }

    // interface for JPA

    protected Point() {
    }

}
