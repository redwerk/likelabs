package com.redwerk.likelabs.infrastructure.messaging;

import com.redwerk.likelabs.application.messaging.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService{

    @Autowired
    JavaMailSender mailSender;

    @Override
    public void sendMessage(String email, String senderEmail, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);

    }
}
