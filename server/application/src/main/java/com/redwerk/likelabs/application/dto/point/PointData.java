package com.redwerk.likelabs.application.dto.point;

import com.redwerk.likelabs.domain.model.point.Address;

public class PointData {
 
    private final Address address;
    
    private final String phone;

    private final String email;

    public PointData(Address address, String phone, String email) {
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

}
