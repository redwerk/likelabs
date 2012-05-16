package com.redwerk.likelabs.infrastructure.messaging.test;

import org.junit.Ignore;
import org.junit.Test;

import com.redwerk.likelabs.application.messaging.SmsService;
import com.redwerk.likelabs.infrastructure.messaging.SmppSmsServiceImpl;
import com.redwerk.likelabs.testutils.ReflectFieldExpose;



public class SmppSmsServiceTest {
	
	private static final String PHONE_NUMBER = "+380000000";
	private static final String MSG_TEXT = "###### TEST MESSAGE #######";
	private static final String HOST = "localhost";
	private static final int PORT = 2775;
	private static final String USERNAME = "smppclient1";
	private static final String PASSWORD = "password";


	@Ignore("Substitute credentials before run, optionally setup test SMPP server (like SMPPSim)")
	@Test
	public void testSmppSendMessage() throws Exception {		
		SmsService smsService = new SmppSmsServiceImpl();		
		new ReflectFieldExpose(smsService)
		.set("remoteHost", HOST)
		.set("remotePort", PORT)
		.set("username", USERNAME)
		.set("password", PASSWORD)
		.set("sourceAddress", "666");
		
		try {
			smsService.sendMessage(PHONE_NUMBER, MSG_TEXT);
			Thread.sleep(5000);	//needed to get delivery results
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}		
	}
}
