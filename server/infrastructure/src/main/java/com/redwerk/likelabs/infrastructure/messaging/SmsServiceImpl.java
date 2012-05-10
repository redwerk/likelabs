package com.redwerk.likelabs.infrastructure.messaging;


import com.redwerk.likelabs.application.messaging.SmsService;
import java.net.URLEncoder;
import java.text.MessageFormat;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmsServiceImpl implements SmsService{

    @Value("#{applicationProperties['sms.service.url']}")
    private String SMS_URL_SENDER_TEMPLATE;

    @Value("#{applicationProperties['sms.service.username']}")
    private String username;

    @Value("#{applicationProperties['sms.service.password']}")
    private String password;

    @Override
    public boolean sendMessage(String recipientPhone, String text) {

        try {
            recipientPhone = recipientPhone.replaceAll("[^\\d]*", "");
            HttpClient httpClient = new HttpClient();
            String url = MessageFormat.format(SMS_URL_SENDER_TEMPLATE, username, password, recipientPhone, URLEncoder.encode(text, "UTF-8"));
            HttpMethod method = new GetMethod(url);
            httpClient.executeMethod(method);
            method.releaseConnection();

        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
