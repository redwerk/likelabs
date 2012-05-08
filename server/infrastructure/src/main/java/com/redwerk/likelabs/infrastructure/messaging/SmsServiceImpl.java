/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.redwerk.likelabs.infrastructure.messaging;


import com.redwerk.likelabs.application.impl.registration.PasswordGenerator;
import com.redwerk.likelabs.application.messaging.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.net.URLEncoder;
import java.text.MessageFormat;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.stereotype.Component;

@Component
public class SmsServiceImpl implements SmsService{

    private static final String SMS_URL_SENDER_TEMPLATE = "http://sms2.cdyne.com/sms.svc/SimpleSMSsend?PhoneNumber=011{0}&Message={1}";

    @Override
    public boolean sendMessage(String recipientPhone, String text) {
        
        try {

            HttpClient httpClient = new HttpClient();
            String url = MessageFormat.format(SMS_URL_SENDER_TEMPLATE, recipientPhone.substring(1), URLEncoder.encode(text, "UTF-8"));
            HttpMethod method = new GetMethod(url);
            httpClient.executeMethod(method);
            method.releaseConnection();

        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
