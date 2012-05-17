package com.redwerk.likelabs.infrastructure.messaging;


import com.redwerk.likelabs.application.messaging.SmsService;
import com.redwerk.likelabs.application.messaging.exception.SmsMessagingException;
import com.redwerk.likelabs.application.messaging.exception.SmsMessagingException.DeliveryStatus;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


public class HttpSmsServiceImpl implements SmsService{

    @Value("#{applicationProperties['sms.service.http.url']}")
    private String SMS_URL_SENDER_TEMPLATE;

    @Value("#{applicationProperties['sms.service.http.username']}")
    private String username;

    @Value("#{applicationProperties['sms.service.http.password']}")
    private String password;

    @Override
    public void sendMessage(String phone, String text) {
        phone = phone.replaceAll("[^\\d]*", "");
        HttpClient httpClient = new HttpClient();
		try {
			String url = MessageFormat.format(SMS_URL_SENDER_TEMPLATE, username, password, phone, URLEncoder.encode(text, "UTF-8"));
			HttpMethod method = new GetMethod(url);
			httpClient.executeMethod(method);
			method.releaseConnection();
		} catch (HttpException e) {
			throw new SmsMessagingException(DeliveryStatus.GENERAL_ERROR, phone, e);
		} catch (IOException e) {
			throw new SmsMessagingException(DeliveryStatus.NETWORK_ERROR, phone, e);
		}
    }
}
