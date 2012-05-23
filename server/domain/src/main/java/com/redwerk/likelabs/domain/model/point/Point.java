package com.redwerk.likelabs.domain.model.point;

import com.redwerk.likelabs.domain.model.company.Company;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "point")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "point_address", joinColumns = @JoinColumn(name = "point_id"))
    private Set<Address> addresses;

    private String phone;

    private String email;
    

    // constructors
    
    public Point(Company company, final Address address, String phone, String email) {
        this.company = company;
        this.addresses = new HashSet<Address>() {{
            add(address);
        }};
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
        return addresses.iterator().next();
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    // modifiers

    public void setAddress(Address address) {
        addresses.clear();
        addresses.add(address);
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    // overrides

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point other = (Point) obj;
        return new EqualsBuilder()
                .append(company, other.company)
                .append(getAddress(), other.getAddress())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(company)
                .append(getAddress())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("company", company)
                .append("address", getAddress())
                .append("phone", phone)
                .append("email", email)
                .toString();
    }

    // interface for JPA

    protected Point() {
    }

}
