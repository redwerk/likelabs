package com.redwerk.likelabs.application.impl.user;

import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.dto.UserCreateData;
import com.redwerk.likelabs.application.dto.UserSocialAccountData;
import com.redwerk.likelabs.application.dto.UserUpdateData;
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

    @Autowired
    private PasswordGenerator passwordGenerator;


    @Override
    public User getUser(long userId) {
        return userRepository.find(userId);
    }

    @Override
    public User getUser(String phone) {
        return userRepository.find(phone);
    }

    @Override
    @Transactional
    public User createUser(UserCreateData userData) {
        User user = new UserFactory().createActivatedUser(userData.getPhone(), passwordGenerator.getPassword());
        user.setEmail(userData.getEmail());
        user.setNotifyIfClient(userData.isNotifyIfClient());
        user.setPublishInSN(userData.isPublishInSN());
        for (UserSocialAccountData sa: userData.getAccounts()) {
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

    @Override
    @Transactional
    public User updateUser(long userId, UserUpdateData userData) {
        User user = userRepository.find(userId);
        user.setPhone(userData.getPhone());
        user.setPassword(userData.getPassword());
        user.setEmail(userData.getEmail());
        user.setNotifyIfClient(userData.isNotifyIfClient());
        user.setPublishInSN(userData.isPublishInSN());
        for (UserSocialAccountData sa: userData.getAccounts()) {
            user.addAccount(new UserSocialAccount(sa.getType(), sa.getAccountId(), sa.getAccessToken(), sa.getName()));
        }
        return user;
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
