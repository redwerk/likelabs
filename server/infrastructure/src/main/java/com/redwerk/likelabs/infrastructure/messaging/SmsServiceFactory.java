package com.redwerk.likelabs.infrastructure.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.redwerk.likelabs.application.messaging.SmsService;

@Configuration
public class SmsServiceFactory {
	
	 @Bean	 
	 protected SmsService newInstance(@Value("#{applicationProperties['sms.service.type']}") String smppServiceType) {
		 if("http".equals(smppServiceType)) {
			 return new HttpSmsServiceImpl();
		 } else if("smpp".equals(smppServiceType)) {
			 return new SmppSmsServiceImpl();
		 }
		 throw new IllegalStateException("Invalid application properties - no implementation exists for SMS service type: '" + smppServiceType + "'");
	 }
}
