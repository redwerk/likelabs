package com.redwerk.likelabs.application.impl.registration;

import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class PasswordGeneratorImpl implements PasswordGenerator {

    private static final int LENGTH_PASSWORD = 8;

    private static final String PASSWORD_CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @Override
    public String getPassword(String phone) {

        Random random = new Random(phone.hashCode());
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < LENGTH_PASSWORD; i++) {
            name.append(PASSWORD_CHAR_SET.charAt(random.nextInt(PASSWORD_CHAR_SET.length())));
        }
        return name.toString();
    }
}
