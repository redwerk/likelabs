package com.redwerk.likelabs.web.ui.controller.dto;

public class CompanyDto {

    private Long id = 0L;
    private String name;
    private String phone;
    private String email;
    private Boolean moderate = false;

    public CompanyDto() {
    }

    public CompanyDto(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public CompanyDto(Long id, String name, String phone, String email, Boolean moderate) {
        if (id != null)
            this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        if (moderate != null)
            this.moderate = moderate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isModerate() {
        return moderate;
    }

    public void setModerate(boolean moderate) {
        this.moderate = moderate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CompanyDto other = (CompanyDto) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.phone == null) ? (other.phone != null) : !this.phone.equals(other.phone)) {
            return false;
        }
        if ((this.email == null) ? (other.email != null) : !this.email.equals(other.email)) {
            return false;
        }
        if (this.moderate != other.moderate) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 71 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 71 * hash + (this.phone != null ? this.phone.hashCode() : 0);
        hash = 71 * hash + (this.email != null ? this.email.hashCode() : 0);
        hash = 71 * hash + (this.moderate ? 1 : 0);
        return hash;
    }
}
