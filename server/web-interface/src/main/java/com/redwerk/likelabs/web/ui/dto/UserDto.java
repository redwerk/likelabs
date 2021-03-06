package com.redwerk.likelabs.web.ui.dto;

import com.redwerk.likelabs.domain.model.event.EventType;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserStatus;
import java.util.HashSet;
import java.util.Set;

public class UserDto {

    Long id;
    String phone;
    String email;
    String password;
    String confirmPassword;
    UserStatus status;

    Boolean postToSn = false;
    Boolean postToEmail = false;
    Boolean eventReviewCreated = false;
    Boolean eventReviewApproved = false;
    Boolean eventClientReviewCreated = false;

    public UserDto() {
    }

    public UserDto(User user){
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.confirmPassword = user.getPassword();
        this.postToSn = user.isPostToSN();
        this.postToEmail = user.isPostToEmail();
        for (EventType event : user.getEnabledEvents()) {
            switch (event) {
                case USER_REVIEW_APPROVED: {
                    this.eventReviewApproved = true;
                    break;
                }
                case USER_REVIEW_CREATED: {
                    this.eventReviewCreated = true;
                    break;
                }
                case CLIENT_REVIEW_CREATED: {
                    this.eventClientReviewCreated = true;
                    break;
                }
            }
        }
    }

    public UserDto(Long id, String phone, String email, String password, String confirmPassword) {
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getEventClientReviewCreated() {
        return eventClientReviewCreated;
    }

    public void setEventClientReviewCreated(Boolean eventClientReviewCreated) {
        this.eventClientReviewCreated = eventClientReviewCreated;
    }

    public Boolean getEventReviewApproved() {
        return eventReviewApproved;
    }

    public void setEventReviewApproved(Boolean eventReviewApproved) {
        this.eventReviewApproved = eventReviewApproved;
    }

    public Boolean getEventReviewCreated() {
        return eventReviewCreated;
    }

    public void setEventReviewCreated(Boolean eventReviewCreated) {
        this.eventReviewCreated = eventReviewCreated;
    }

    public Boolean getPostToSn() {
        return postToSn;
    }

    public void setPostToSn(Boolean postToSn) {
        this.postToSn = postToSn;
    }

    public Boolean getPostToEmail() {
        return postToEmail;
    }

    public void setPostToEmail(Boolean postToEmail) {
        this.postToEmail = postToEmail;
    }

    public Set<EventType> getEnabledEvents() {
        Set<EventType> result = new HashSet<EventType>();
        if (this.eventReviewCreated) {
            result.add(EventType.USER_REVIEW_CREATED);
        }
        if (this.eventReviewApproved) {
            result.add(EventType.USER_REVIEW_APPROVED);
        }
        if (this.eventClientReviewCreated) {
            result.add(EventType.CLIENT_REVIEW_CREATED);
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserDto other = (UserDto) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.phone == null) ? (other.phone != null) : !this.phone.equals(other.phone)) {
            return false;
        }
        if ((this.email == null) ? (other.email != null) : !this.email.equals(other.email)) {
            return false;
        }
        if ((this.password == null) ? (other.password != null) : !this.password.equals(other.password)) {
            return false;
        }
        if ((this.confirmPassword == null) ? (other.confirmPassword != null) : !this.confirmPassword.equals(other.confirmPassword)) {
            return false;
        }
        if (this.status != other.status) {
            return false;
        }
        if (this.postToSn != other.postToSn && (this.postToSn == null || !this.postToSn.equals(other.postToSn))) {
            return false;
        }
        if (this.postToEmail != other.postToEmail && (this.postToEmail == null || !this.postToEmail.equals(other.postToEmail))) {
            return false;
        }
        if (this.eventReviewCreated != other.eventReviewCreated && (this.eventReviewCreated == null || !this.eventReviewCreated.equals(other.eventReviewCreated))) {
            return false;
        }
        if (this.eventReviewApproved != other.eventReviewApproved && (this.eventReviewApproved == null || !this.eventReviewApproved.equals(other.eventReviewApproved))) {
            return false;
        }
        if (this.eventClientReviewCreated != other.eventClientReviewCreated && (this.eventClientReviewCreated == null || !this.eventClientReviewCreated.equals(other.eventClientReviewCreated))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 79 * hash + (this.phone != null ? this.phone.hashCode() : 0);
        hash = 79 * hash + (this.email != null ? this.email.hashCode() : 0);
        return hash;
    }
}
