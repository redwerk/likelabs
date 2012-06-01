package com.redwerk.likelabs.domain.model.user;

import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.user.exception.DuplicatedUserException;

import java.util.List;

public abstract class UserRepository {
    
    public abstract User get(long id);

    public abstract User find(String phone);

    public abstract List<User> findAll(Pager pager);

    public abstract List<User> findRegular(Pager pager);

    public abstract int getCount();

    public abstract int getRegularCount();

    public void add(User user) {
        if (find(user.getPhone()) != null) {
            throw new DuplicatedUserException(user.getPhone(), user);
        }
        addInternal(user);
    }

    protected abstract void addInternal(User user);

    public abstract void remove(User user);

}
