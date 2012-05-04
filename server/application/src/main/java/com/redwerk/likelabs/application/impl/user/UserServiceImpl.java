package com.redwerk.likelabs.application.impl.user;

import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.dto.UserSocialAccountData;
import com.redwerk.likelabs.application.dto.UserData;
import com.redwerk.likelabs.application.impl.registration.PasswordGenerator;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.model.photo.PhotoRepository;
import com.redwerk.likelabs.domain.model.photo.PhotoStatus;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserFactory;
import com.redwerk.likelabs.domain.model.user.UserRepository;
import com.redwerk.likelabs.domain.model.user.UserSocialAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhotoRepository photoRepository;


    @Override
    public User getUser(long userId) {
        return userRepository.find(userId);
    }

    @Override
    public User getUser(String phone) {
        return userRepository.find(phone);
    }

    /*
    @Override
    @Transactional
    public User createUser(String phone, String password, String email, List<UserSocialAccountData> accounts) {
        User user = new UserFactory().createActivatedUser(phone, passwordGenerator.getPassword());
        user.setEmail(email);
        for (UserSocialAccountData sa: accounts) {
            user.addAccount(new UserSocialAccount(sa.getType(), sa.getAccountId(), sa.getAccessToken(), sa.getName()));
        }
        return user;
    }

    @Override
    @Transactional
    public User confirmUser(String phone, String password) {
        User user = userRepository.find(phone);
        if (!user.getPassword().equals(password)) {
            return null;
        }
        user.activate();
        return user;
    }
    */

    @Override
    @Transactional
    public void updateUser(long userId, UserData userData) {
        User user = userRepository.find(userId);
        user.setPhone(userData.getPhone());
        user.setPassword(userData.getPassword());
        user.setEmail(userData.getEmail());
        user.setNotifyIfClient(userData.isNotifyIfClient());
        user.setPublishInSN(userData.isPublishInSN());
    }

    @Override
    @Transactional
    public void attachToSN(long userId, UserSocialAccountData account) {

    }

    @Override
    @Transactional
    public void detachFromSN(long userId, SocialNetworkType snType) {

    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        userRepository.remove(userRepository.find(userId));
    }

    @Override
    public List<Photo> getUserPhotos(long userId, PhotoStatus photoStatus) {
        User user = userRepository.find(userId);
        return photoRepository.findAll(user, photoStatus);
    }

}
