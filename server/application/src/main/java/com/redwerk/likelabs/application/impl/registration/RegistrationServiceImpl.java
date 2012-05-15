package com.redwerk.likelabs.application.impl.registration;

import com.redwerk.likelabs.application.RegistrationService;
import com.redwerk.likelabs.application.impl.registration.exception.DuplicatedUserException;
import com.redwerk.likelabs.application.impl.registration.exception.IncorrectPasswordException;
import com.redwerk.likelabs.application.impl.registration.exception.NoSendSmsException;
import com.redwerk.likelabs.application.impl.registration.exception.AbsentSocialAccountException;
import com.redwerk.likelabs.application.impl.registration.exception.PageAccessLevelException;
import com.redwerk.likelabs.application.impl.registration.exception.AbsentCompanyException;
import com.redwerk.likelabs.application.impl.registration.exception.NotConfirmMailException;
import com.redwerk.likelabs.application.messaging.MessageTemplateService;
import com.redwerk.likelabs.application.messaging.SmsService;
import com.redwerk.likelabs.application.sn.GatewayFactory;
import com.redwerk.likelabs.application.sn.SocialNetworkGateway;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.company.CompanyRepository;
import com.redwerk.likelabs.domain.model.company.CompanySocialPage;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserFactory;
import com.redwerk.likelabs.domain.model.user.UserRepository;
import com.redwerk.likelabs.domain.model.user.UserSocialAccount;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RegistrationServiceImpl implements RegistrationService {

    private static final String MSG_SMS_REG = "message.sms.registration";

    private static final String MSG_APP_DOMAIN =  "app.domain";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    private PasswordGenerator passwordGenerator;

    @Autowired
    CodeGenerator codeGenerator;

    @Autowired
    GatewayFactory gatewayFactory;

    @Autowired
    private SmsService smsService;

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Override
    public void createUser(String phone) {
        if (userRepository.find(phone) != null) {
            throw new DuplicatedUserException(phone);
        }
        String msg = messageTemplateService.getMessage(
                MSG_SMS_REG, messageTemplateService.getMessage(MSG_APP_DOMAIN) ,passwordGenerator.getPassword(phone));
        if (!smsService.sendMessage(phone, msg)) {
            throw new NoSendSmsException(phone);
        }
    }

    @Override
    @Transactional
    public void activateUser(String phone, String password) {
        if (!password.equals(passwordGenerator.getPassword(phone))) {
            throw new IncorrectPasswordException(phone, password);
        }
        userRepository.add(new UserFactory().createActivatedUser(phone, password));
    }

    @Override
    @Transactional
    public void confirmEmail(long userId, String email, String confirmationCode) {
        if (!confirmationCode.equals(codeGenerator.getConfirmEmailCode(email, userId))) {
            throw new NotConfirmMailException(userId, email, confirmationCode);
        }
        User user = userRepository.get(userId);
        user.setEmail(email);
    }

    @Override
    @Transactional
    public void activateCompanyAdmin(long userId) {
        User user = userRepository.get(userId);
        List<UserSocialAccount> accounts = user.getAccounts();
        if (accounts.size() == 0) {
            throw new AbsentSocialAccountException(user);
        }
        List<Company> companies = companyRepository.findAll(user, -1, -1);
        if (companies.size() == 0) {
            throw new AbsentCompanyException(user);
        }
        for (Company company : companies) {
            for (UserSocialAccount account : accounts) {
                Set<CompanySocialPage> pages = company.getSocialPages(account.getType());
                for (CompanySocialPage page : pages) {
                    if (!gatewayFactory.getGateway(account.getType()).isAdminFor(account, page)) {
                        throw new PageAccessLevelException(user, page);
                    }
                }
            }
        }
        user.activate();
    }

    @Override
    public boolean validateAdminCode(long userId, String activateCode) {
        User user = userRepository.get(userId);
        return activateCode.equals(codeGenerator.getActivateAdminCode(userId, user.getEmail(), user.getPhone()));

    }

    @Override
    public boolean validateAdminPassword(long userId, String password) {
        User user = userRepository.get(userId);
        return password.equals(user.getPassword());
    }
}
