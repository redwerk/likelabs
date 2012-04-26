package com.redwerk.likelabs.application.impl;

import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.dto.UserData;
import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.model.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(long userId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public User getUser(String userPhone) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public User createUser(UserData userData) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteUser(long userId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateUser(long userId, UserData userData) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Photo> getUserPhotos(long userId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
