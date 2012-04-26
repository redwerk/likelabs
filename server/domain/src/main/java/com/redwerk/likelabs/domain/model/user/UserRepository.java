package com.redwerk.likelabs.domain.model.user;

import java.util.List;

public interface UserRepository {

    User findByPhone(String phone);

    List<User> findAll();

    void add(User user);

    void remove(User user);

}
