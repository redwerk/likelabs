package com.redwerk.likelabs.infrastructure.messaging;

import com.redwerk.likelabs.application.messaging.SmsService;
import com.redwerk.likelabs.application.messaging.exception.SmsMessagingException;
import com.redwerk.likelabs.application.messaging.exception.SmsMessagingException.DeliveryStatus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.beans.factory.annotation.Value;

public class HttpSmsServiceImpl implements SmsService {

    @Value("#{applicationProperties['sms.service.http.url']}")
    private String urlTemplate;

    @Value("#{applicationProperties['sms.service.http.username']}")
    private String username;

    @Value("#{applicationProperties['sms.service.http.password']}")
    private String password;

    @Override
    public void sendMessage(String phone, String text) {
        String parsedPhone = phone.replaceAll("[^\\d]*", "");
        HttpMethod method = null;
		try {
            HttpClient httpClient = new HttpClient();
            String url = MessageFormat.format(urlTemplate, username, password, parsedPhone, URLEncoder.encode(text, "UTF-8"));
            method = new GetMethod(url);
			httpClient.executeMethod(method);
        } catch (UnsupportedEncodingException e) {
            throw new SmsMessagingException(DeliveryStatus.GENERAL_ERROR, parsedPhone, e);
		} catch (HttpException e) {
			throw new SmsMessagingException(DeliveryStatus.GENERAL_ERROR, parsedPhone, e);
        } catch (IOException e) {
			throw new SmsMessagingException(DeliveryStatus.NETWORK_ERROR, parsedPhone, e);
		}
        finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
    }
}
