package com.redwerk.likelabs.application.impl.registration;

import com.redwerk.likelabs.application.RegistrationService;
import com.redwerk.likelabs.application.messaging.SmsService;
import com.redwerk.likelabs.domain.model.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordGenerator passwordGenerator;

    //@Autowired
    //private SmsService smsService;

    @Override
    public void createUser(String phone) {
        // TODO: send SMS with generated password (based on phone) to specified phone number
    }

    @Override
    public void activateUser(String phone, String password) {
        // TODO: regenerate password (by phone),
        // compare it with specified password,
        // invoke "new UserFactory().createActivatedUser(phone, password);" if all is correct
    }

    @Override
    public void confirmEmail(long userId, String confirmationCode) {
        // TODO: extract email from confirmationCode,
        // validate userId and email using confirmationCode,
        // invoke "user.setEmail(email)" if all is correct
    }

}
