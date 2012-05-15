package com.redwerk.likelabs.application.impl.user;

import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.dto.UserData;
import com.redwerk.likelabs.application.impl.registration.CodeGenerator;
import com.redwerk.likelabs.application.impl.registration.PasswordGenerator;
import com.redwerk.likelabs.application.messaging.EmailService;
import com.redwerk.likelabs.application.messaging.MessageTemplateService;
import com.redwerk.likelabs.application.sn.GatewayFactory;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.model.photo.PhotoRepository;
import com.redwerk.likelabs.domain.model.photo.PhotoStatus;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserRepository;
import java.text.MessageFormat;

import com.redwerk.likelabs.domain.model.user.UserSocialAccount;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final String LINK_ACTIVATE_EMAIL_TEMPLATE = "{0}/activatemail?id={1}&email={2}&activatecode={3}";
    private static final String MSG_EMAIL_BODY = "message.email.registration.body";
    private static final String MSG_EMAIL_SUBJECT = "message.email.registration.subject";
    private static final String MSG_EMAIL_FROM = "message.email.registration.mailfrom";
    private static final String MSG_APP_DOMAIN =  "app.domain";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    GatewayFactory gatewayFactory;

    @Autowired
    CodeGenerator codeGenerator;


    @Override
    @Transactional
    public User getUser(long userId) {
        User user = userRepository.get(userId);
        return getLoadedUser(user);
    }

    @Override
    @Transactional
    public User findUser(String phone) {
        User user = userRepository.find(phone);
        return (user != null) ? getLoadedUser(user) : null;
    }
    
    private User getLoadedUser(User user) {
        user.getAccounts();
        return user;
    }

    @Override
    @Transactional
    public void updateUser(long userId, UserData userData) {
        if (userData == null) {
            throw new IllegalArgumentException("userData cannot be null");
        }
        User user = userRepository.get(userId);
        user.setPhone(userData.getPhone());
        user.setPassword(userData.getPassword());
        user.setEnabledEvents(userData.getEnabledEvents());
        user.setPublishInSN(userData.isPublishInSN());
        doEmailUpdate(user, userData.getEmail());
    }

    @Override
    public void updateEmail(long userId, String email) {
        doEmailUpdate(userRepository.get(userId), email);
    }
    
    private void doEmailUpdate(User user, String email) {
        if (StringUtils.equals(email, user.getEmail())) {
            return;
        }
        if (StringUtils.isEmpty(email)) {
            user.setEmail(null);
            return;
        }
        Long userId = user.getId();
        String activateLink = MessageFormat.format(LINK_ACTIVATE_EMAIL_TEMPLATE,
                messageTemplateService.getMessage(MSG_APP_DOMAIN), userId, email,
                codeGenerator.getConfirmEmailCode(email, userId));
        emailService.sendMessage(email, messageTemplateService.getMessage(MSG_EMAIL_FROM),
                messageTemplateService.getMessage(MSG_EMAIL_SUBJECT),
                messageTemplateService.getMessage(MSG_EMAIL_BODY, activateLink));
    }

    @Override
    @Transactional
    public UserSocialAccount attachAccount(long userId, SocialNetworkType snType, String accessCode) {
        User user = userRepository.get(userId);
        UserSocialAccount account = gatewayFactory.getGateway(snType).getUserAccount(accessCode);
        user.addAccount(account);
        return account;
    }

    @Override
    @Transactional
    public void detachAccount(long userId, SocialNetworkType snType) {
        if (snType == null) {
            throw new IllegalArgumentException("snType cannot be null");
        }
        User user = userRepository.get(userId);
        user.removeAccount(snType);
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        User user = userRepository.get(userId);
        userRepository.remove(user);
    }

    @Override
    public List<Photo> getPhotos(long userId, PhotoStatus photoStatus) {
        User user = userRepository.get(userId);
        return photoRepository.findAll(user, photoStatus, -1, -1);
    }

    @Override
    public void updatePhoto(long photoId, PhotoStatus photoStatus) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
