package com.redwerk.likelabs.application.impl.registration;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class ActivateEmailCodeGeneratorImpl implements ActivateEmailCodeGenerator {

    private static final String SALT_EMAIL_VERIFY = "SALT_LakeLabs";

    @Override
    public String getActivateEmailCode(String email, long userId) {

        return DigestUtils.md5Hex(email + String.valueOf(userId) + SALT_EMAIL_VERIFY);
    }
}
