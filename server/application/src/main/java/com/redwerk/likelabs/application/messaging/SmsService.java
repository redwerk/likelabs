package com.redwerk.likelabs.application.messaging;

public interface SmsService {
    
    boolean sendMessage(String recipientPhone, String text);

}
