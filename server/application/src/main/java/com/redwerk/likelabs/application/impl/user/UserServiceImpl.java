package com.redwerk.likelabs.application.impl.user;

import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.dto.UserData;
import com.redwerk.likelabs.application.impl.registration.ActivateEmailCodeGenerator;
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
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;

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
    ActivateEmailCodeGenerator activateEmailCodeGenerator;
    
    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private PasswordGenerator passwordGenerator;

    @Autowired
    @Qualifier(value="gatewayFactory")
    GatewayFactory gatewayFactory;

    @Override
    @Transactional
    public User getUser(long userId) {
        User user = userRepository.find(userId);
        if (user == null) {
            throw new IllegalStateException("User with id = " + userId + " is not found");
        }
        return getLoadedUser(user);
    }

    @Override
    @Transactional
    public User findUser(String phone) {
        return getLoadedUser(userRepository.find(phone));
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
        String activateLink = MessageFormat.format(LINK_ACTIVATE_EMAIL_TEMPLATE, messageTemplateService.getMessage(MSG_APP_DOMAIN),
                                                               userId, email, activateEmailCodeGenerator.getActivateEmailCode(email, userId));
        emailService.sendMessage(email, messageTemplateService.getMessage(MSG_EMAIL_FROM), messageTemplateService.getMessage(MSG_EMAIL_SUBJECT),
                                      messageTemplateService.getMessage(MSG_EMAIL_BODY, activateLink));
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
    }

    @Override
    @Transactional
    public void attachToSN(long userId, SocialNetworkType snType, String accessCode) {
        User user = userRepository.find(userId);
        if (user == null) {
            throw new IllegalStateException("User with id = " + userId + " is not found");
        }
        user.addAccount(gatewayFactory.getGateway(snType).getUserAccount(accessCode));
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
