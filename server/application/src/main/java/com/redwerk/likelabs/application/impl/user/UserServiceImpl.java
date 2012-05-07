package com.redwerk.likelabs.application.impl.user;

import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.dto.UserSocialAccountData;
import com.redwerk.likelabs.application.dto.UserData;
import com.redwerk.likelabs.application.impl.registration.PasswordGenerator;
import com.redwerk.likelabs.application.messaging.EmailService;
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

    //@Autowired
    //private EmailService emailService;

    @Override
    public User getUser(long userId) {
        User user = userRepository.find(userId);
        if (user == null) {
            throw new IllegalStateException("User with id = " + userId + " is not found");
        }
        return user;
    }

    @Override
    public User findUser(String phone) {
        return userRepository.find(phone);
    }

    @Override
    @Transactional
    public void updateUser(long userId, UserData userData) {
        if (userData == null) {
            throw new IllegalArgumentException("userData cannot be null");
        }
        User user = userRepository.find(userId);
        if (user == null) {
            throw new IllegalStateException("User with id = " + userId + " is not found");
        }
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
        if (accountData == null) {
            throw new IllegalArgumentException("accountData cannot be null");
        }
        User user = userRepository.find(userId);
        if (user == null) {
            throw new IllegalStateException("User with id = " + userId + " is not found");
        }
        user.addAccount(new UserSocialAccount(accountData.getType(), accountData.getAccountId(),
                accountData.getAccessToken(), accountData.getName()));
    }

    @Override
    @Transactional
    public void detachFromSN(long userId, SocialNetworkType snType) {
        if (snType == null) {
            throw new IllegalArgumentException("snType cannot be null");
        }
        User user = userRepository.find(userId);
        if (user == null) {
            throw new IllegalStateException("User with id = " + userId + " is not found");
        }
        user.removeAccount(snType);
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        User user = userRepository.find(userId);
        if (user == null) {
            throw new IllegalStateException("User with id = " + userId + " is not found");
        }
        userRepository.remove(user);
    }

    @Override
    public List<Photo> getUserPhotos(long userId, PhotoStatus photoStatus) {
        User user = userRepository.find(userId);
        if (user == null) {
            throw new IllegalStateException("User with id = " + userId + " is not found");
        }
        return photoRepository.findAll(user, photoStatus);
    }

}
