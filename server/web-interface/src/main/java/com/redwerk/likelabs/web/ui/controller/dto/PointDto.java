package com.redwerk.likelabs.web.ui.controller.dto;

import com.redwerk.likelabs.application.dto.point.PointData;
import com.redwerk.likelabs.domain.model.point.Address;
import com.redwerk.likelabs.domain.model.point.Point;
import org.apache.commons.lang.StringUtils;


public class PointDto {

    private Long id = 0L;

    private String city = null;

    private String state = null;

    private String country = null;

    private String postalCode = null;

    private String addressLine1 = null;

    private String addressLine2 = null;

    private String phone = null;

    private String email = null;

    public PointDto() {
    }

    public PointDto(Point p) {
        Address a = p.getAddress();
        if (a != null) {
            this.city = a.getCity();
            this.state = a.getState();
            this.country = a.getCountry();
            this.postalCode = a.getPostalCode();
            this.addressLine1 = a.getAddressLine1();
            this.addressLine2 = a.getAddressLine2();
        }
        this.id = p.getId();
        this.phone = p.getPhone();
        this.email = p.getEmail();

        
    }

    public PointData getPointData() {
        Address a = new Address(city, state, country, postalCode, addressLine1, addressLine2);
        return new PointData(a, phone, email);
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = (StringUtils.isNotBlank(addressLine1)) ? addressLine1.trim() : null;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = (StringUtils.isNotBlank(addressLine2)) ? addressLine2.trim() : null;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = (StringUtils.isNotBlank(city)) ? city.trim() : null;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = (StringUtils.isNotBlank(country)) ? country.trim() : null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = (StringUtils.isNotBlank(email)) ? email.trim() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = (StringUtils.isNotBlank(phone)) ? phone.trim() : null;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = (StringUtils.isNotBlank(postalCode)) ? postalCode.trim() : null;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = (StringUtils.isNotBlank(state)) ? state.trim() : null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PointDto other = (PointDto) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.city == null) ? (other.city != null) : !this.city.equals(other.city)) {
            return false;
        }
        if ((this.state == null) ? (other.state != null) : !this.state.equals(other.state)) {
            return false;
        }
        if ((this.country == null) ? (other.country != null) : !this.country.equals(other.country)) {
            return false;
        }
        if ((this.postalCode == null) ? (other.postalCode != null) : !this.postalCode.equals(other.postalCode)) {
            return false;
        }
        if ((this.addressLine1 == null) ? (other.addressLine1 != null) : !this.addressLine1.equals(other.addressLine1)) {
            return false;
        }
        if ((this.addressLine2 == null) ? (other.addressLine2 != null) : !this.addressLine2.equals(other.addressLine2)) {
            return false;
        }
        if ((this.phone == null) ? (other.phone != null) : !this.phone.equals(other.phone)) {
            return false;
        }
        if ((this.email == null) ? (other.email != null) : !this.email.equals(other.email)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 13 * hash + (this.city != null ? this.city.hashCode() : 0);
        hash = 13 * hash + (this.state != null ? this.state.hashCode() : 0);
        hash = 13 * hash + (this.country != null ? this.country.hashCode() : 0);
        hash = 13 * hash + (this.postalCode != null ? this.postalCode.hashCode() : 0);
        hash = 13 * hash + (this.addressLine1 != null ? this.addressLine1.hashCode() : 0);
        hash = 13 * hash + (this.addressLine2 != null ? this.addressLine2.hashCode() : 0);
        hash = 13 * hash + (this.phone != null ? this.phone.hashCode() : 0);
        hash = 13 * hash + (this.email != null ? this.email.hashCode() : 0);
        return hash;
    }
}
