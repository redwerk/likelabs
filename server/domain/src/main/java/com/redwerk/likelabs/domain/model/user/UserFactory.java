package com.redwerk.likelabs.domain.model.user;

public class UserFactory {
    
    public User createActivatedUser(String phone, String password) {
        return new User(phone, password, true);
    }

    public User createUser(String phone, String password) {
        return new User(phone, password, false);
    }

}
