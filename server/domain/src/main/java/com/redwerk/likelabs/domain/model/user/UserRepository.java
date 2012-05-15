package com.redwerk.likelabs.domain.model.user;

import java.util.List;

public interface UserRepository {

    User get(long id);

    User find(String phone);

    List<User> findAll(int offset, int count);

    void add(User user);

    void remove(User user);

}
