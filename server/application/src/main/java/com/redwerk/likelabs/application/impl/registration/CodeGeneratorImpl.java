package com.redwerk.likelabs.application.impl.registration;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class CodeGeneratorImpl implements CodeGenerator {

    private static final String SALT_CODE = "SALT_LakeLabs";

    @Override
    public String getEmailConfirmationCode(String email, long userId) {

        return DigestUtils.md5Hex(email + String.valueOf(userId) + SALT_CODE);
    }

    @Override
    public String getAdminActivationCode(long id, String email, String phone) {
        
        return DigestUtils.md5Hex(email + String.valueOf(id) + phone + SALT_CODE);
    }
}
