package com.redwerk.likelabs.application.impl.registration;

import com.redwerk.likelabs.application.RegistrationService;
import com.redwerk.likelabs.application.impl.registration.exception.DuplicatedUserException;
import com.redwerk.likelabs.application.impl.registration.exception.IncorrectPasswordException;
import com.redwerk.likelabs.application.impl.registration.exception.NoSendSmsException;
import com.redwerk.likelabs.application.impl.registration.exception.NotConfirmMailException;
import com.redwerk.likelabs.application.messaging.MessageTemplateService;
import com.redwerk.likelabs.application.messaging.SmsService;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserFactory;
import com.redwerk.likelabs.domain.model.user.UserRepository;
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
    private PasswordGenerator passwordGenerator;

    @Autowired
    ActivateEmailCodeGenerator activateEmailCodeGenerator;

    @Autowired
    private SmsService smsService;

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Override
    public void createUser(String phone) {

        if (userRepository.find(phone) != null) {
            throw new DuplicatedUserException(phone);
        }
        String msg = messageTemplateService.getMessage(MSG_SMS_REG, messageTemplateService.getMessage(MSG_APP_DOMAIN) ,passwordGenerator.getPassword(phone));
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

        if (!confirmationCode.equals(activateEmailCodeGenerator.getActivateEmailCode(email, userId))) {
            throw new NotConfirmMailException(userId,email,confirmationCode);
        }
        User user = userRepository.find(userId);
        user.setEmail(email);
    }
}
