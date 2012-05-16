package com.redwerk.likelabs.application.messaging;

public interface EmailService {
    
    void sendMessage(String email, String senderEmail, String subject, String text);

}
