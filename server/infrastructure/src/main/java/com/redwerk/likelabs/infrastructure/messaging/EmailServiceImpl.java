package com.redwerk.likelabs.infrastructure.messaging;

import com.redwerk.likelabs.application.messaging.EmailService;
import com.redwerk.likelabs.application.messaging.exception.EmailMessagingException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender mailSender;

    private final Logger log = LogManager.getLogger(getClass());

    @Override
    public void sendMessage(String email, String senderEmail, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        try {
            mailSender.send(message);
        } catch (MailSendException e) {
            log.error(e,e);
            throw new EmailMessagingException(email);
        }
    }
}
