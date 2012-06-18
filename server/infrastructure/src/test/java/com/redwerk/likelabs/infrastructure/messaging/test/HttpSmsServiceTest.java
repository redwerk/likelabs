package com.redwerk.likelabs.infrastructure.messaging.test;

import org.junit.Ignore;
import org.junit.Test;

import com.redwerk.likelabs.application.messaging.SmsService;
import com.redwerk.likelabs.infrastructure.messaging.HttpSmsServiceImpl;
import com.redwerk.likelabs.testutils.ReflectFieldExpose;



public class HttpSmsServiceTest {
	
	private static final String PHONE_NUMBER = "+38000000000";
	private static final String MSG_TEXT = "###### TEST MESSAGE #######";
	private static final String USERNAME = "smppclient1";
	private static final String PASSWORD = "password";

	
	@Ignore("Substitute credentials before run")
	@Test
	public void testHttpSendMessage() {
		SmsService smsService = new HttpSmsServiceImpl();
		try {
			new ReflectFieldExpose(smsService)
				.set("urlTemplate", "http://sms2.cdyne.com/sms.svc/SimpleSMSsend?PhoneNumber=011{2}&Message={3}")
				.set("username", USERNAME)
				.set("password", PASSWORD);
			
			smsService.sendMessage(PHONE_NUMBER, MSG_TEXT);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
