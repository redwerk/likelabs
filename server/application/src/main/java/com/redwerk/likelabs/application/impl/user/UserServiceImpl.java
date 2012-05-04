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
import org.apache.commons.lang.StringUtils;
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

    @Override
    @Transactional
    public void updateUser(long userId, UserData userData) {
        User user = userRepository.find(userId);
        user.setPhone(userData.getPhone());
        user.setPassword(userData.getPassword());
        user.setNotifyIfClient(userData.isNotifyIfClient());
        user.setPublishInSN(userData.isPublishInSN());
        doEmailUpdate(user, userData.getEmail());
    }

    @Override
    public void updateUserEmail(long userId, String email) {
        doEmailUpdate(userRepository.find(userId), email);
    }
    
    private void doEmailUpdate(User user, String email) {
        if (StringUtils.equals(email, user.getEmail())) {
            return;
        }
        if (StringUtils.isEmpty(email)) {
            user.setEmail(null);
            return;
        }
        // TODO: add implementation (send confirmation email)
    }

    @Override
    @Transactional
    public void attachToSN(long userId, UserSocialAccountData accountData) {
        User user = userRepository.find(userId);
        UserSocialAccount account = user.findAccount(accountData.getType());
        if (account != null) {
            throw new IllegalArgumentException("user with id = " + userId + " is already attached to network type " + accountData.getType());
        }
        user.addAccount(account);
    }

    @Override
    @Transactional
    public void detachFromSN(long userId, SocialNetworkType snType) {
        User user = userRepository.find(userId);
        UserSocialAccount account = user.findAccount(snType);
        if (account == null) {
            throw new IllegalArgumentException("user with id = " + userId + " is not attached to network type " + snType);
        }
        user.removeAccount(account);
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
