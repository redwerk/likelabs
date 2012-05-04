package com.redwerk.likelabs.application.messaging;

public interface EmailService {
    
    void sendMessage(String recipientEmail, String senderEmail, String subject, String text);

}
