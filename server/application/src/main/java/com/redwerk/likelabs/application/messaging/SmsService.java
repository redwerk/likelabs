package com.redwerk.likelabs.application.messaging;

public interface SmsService {
	
    void sendMessage(String phone, String text);

}
