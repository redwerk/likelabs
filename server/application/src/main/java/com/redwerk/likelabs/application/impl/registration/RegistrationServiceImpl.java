package com.redwerk.likelabs.application.impl.registration;

import com.redwerk.likelabs.application.RegistrationService;
import com.redwerk.likelabs.domain.model.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordGenerator passwordGenerator;


    @Override
    public void createUser(String phone) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void activateUser(String phone, String password) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setEmail(long userId, String email) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void confirmEmail(long userId, String activationCode) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
