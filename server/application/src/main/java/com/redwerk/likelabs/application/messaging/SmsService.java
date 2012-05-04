package com.redwerk.likelabs.application.messaging;

public interface SmsService {
    
    void sendMessage(String recipientPhone, String text);

}
