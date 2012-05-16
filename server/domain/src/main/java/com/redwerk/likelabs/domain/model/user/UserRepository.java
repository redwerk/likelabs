package com.redwerk.likelabs.domain.model.user;

import com.redwerk.likelabs.domain.model.query.Pager;

import java.util.List;

public interface UserRepository {

    User get(long id);

    User find(String phone);

    List<User> findAll(Pager pager);
    
    int getCount();

    void add(User user);

    void remove(User user);

}
