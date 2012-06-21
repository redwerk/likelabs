package com.redwerk.likelabs.application.impl;

import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.user.UserProfileData;
import com.redwerk.likelabs.application.dto.user.UserSettingsData;
import com.redwerk.likelabs.application.impl.registration.CodeGenerator;
import com.redwerk.likelabs.application.messaging.EmailService;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.company.CompanyRepository;
import com.redwerk.likelabs.domain.model.review.ReviewRepository;
import com.redwerk.likelabs.domain.model.user.*;
import com.redwerk.likelabs.domain.service.sn.GatewayFactory;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.query.Pager;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private static final String LINK_ACTIVATE_EMAIL_TEMPLATE = "{0}/activatemail?id={1}&email={2}&activatecode={3}";

    private static final String EMAIL_ACTIVATION_BODY_MSG = "message.email.registration.user.body";
    private static final String EMAIL_ACTIVATION_SUBJECT_MSG = "message.email.registration.user.subject";
    private static final String EMAIL_ACTIVATION_FROM_MSG = "message.email.registration.from";

    private static final String APP_DOMAIN_MSG =  "app.domain";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private GatewayFactory gatewayFactory;

    @Autowired
    private CodeGenerator codeGenerator;


    @Override
    @Transactional(readOnly = true)
    public Report<User> getRegularUsers(Pager pager) {
        return new Report<User>(
                userRepository.findRegular(pager),
                userRepository.getRegularCount()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(long userId) {
        User user = userRepository.get(userId);
        return getLoadedUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(SocialNetworkType snType, String accountId) {
        Validate.notNull(snType, "snType cannot be null");
        return userRepository.get(snType, accountId);
    }

    @Override
    @Transactional(readOnly = true)
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
    public User createUser(long creatorId, UserProfileData userProfile) {
        User creator = userRepository.get(creatorId);
        Validate.isTrue(creator.isSystemAdmin(), "Only system administrators can create activated users");

        User user = new UserFactory().createActivatedUser(userProfile.getPhone(), userProfile.getPassword());
        user.setEmail(userProfile.getEmail());

        userRepository.add(user);
        return user;
    }

    @Override
    @Transactional
    public void updateProfile(long userId, UserProfileData userProfile) {
        Validate.notNull(userProfile, "userProfile cannot be null");
        User user = userRepository.get(userId);
        user.setPhone(userProfile.getPhone());
        String newPassword = userProfile.getPassword();
        if (newPassword != null) {
            user.setPassword(newPassword);
        }
        doEmailUpdate(user, userProfile.getEmail());
    }

    @Override
    @Transactional
    public void updateSettings(long userId, UserSettingsData userSettings) {
        Validate.notNull(userSettings, "userSettings cannot be null");
        User user = userRepository.get(userId);
        user.setEnabledEvents(userSettings.getEnabledEvents());
        user.setPublishInSN(userSettings.isPublishInSN());
    }

    @Override
    @Transactional
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
                messageTemplateService.getMessage(APP_DOMAIN_MSG), userId, email,
                codeGenerator.getEmailConfirmationCode(email, userId));
        emailService.sendMessage(email, messageTemplateService.getMessage(EMAIL_ACTIVATION_FROM_MSG),
                messageTemplateService.getMessage(EMAIL_ACTIVATION_SUBJECT_MSG),
                messageTemplateService.getMessage(EMAIL_ACTIVATION_BODY_MSG, activateLink));
    }

    @Override
    @Transactional
    public void updateStatus(long userId, long updaterId, UserStatus status) {
        Validate.notNull(status);
        Validate.isTrue(status == UserStatus.ACTIVE || status == UserStatus.DELETED);

        User user = userRepository.get(userId);
        if (user.getStatus() == status) {
            return;
        }

        User updater = userRepository.get(updaterId);

        if (status == UserStatus.ACTIVE) {
            Validate.isTrue(updater.isSystemAdmin());
            if (user.getStatus() == UserStatus.NOT_ACTIVATED) {
                user.activate();
            }
            else {
                user.restore();
            }
        }
        else if (status == UserStatus.DELETED) {
            Validate.isTrue(user.equals(updater) || updater.isSystemAdmin());
            userRepository.remove(user, reviewRepository);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAdminFor(long userId, long companyId) {
        Company company = companyRepository.get(companyId);
        for (User user: company.getAdmins()) {
            if (user.getId() == userId) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public UserSocialAccount attachAccount(long userId, SocialNetworkType snType, String accessCode) {
        Validate.notNull(snType, "snType cannot be null");
        Validate.notNull(accessCode, "accessCode cannot be null");
        User user = userRepository.get(userId);
        UserSocialAccount account = gatewayFactory.getGateway(snType).getUserAccountByCode(accessCode);
        user.addAccount(account);
        return account;
    }

    @Override
    @Transactional
    public UserSocialAccount attachAccount(long userId, UserSocialAccount account) {
        Validate.notNull("account cannot be null");
        User user = userRepository.get(userId);
        user.addAccount(account);
        return account;
    }

    @Override
    @Transactional
    public void detachAccount(long userId, SocialNetworkType snType) {
        Validate.notNull(snType, "snType cannot be null");
        User user = userRepository.get(userId);
        user.removeAccount(snType);
    }

}
