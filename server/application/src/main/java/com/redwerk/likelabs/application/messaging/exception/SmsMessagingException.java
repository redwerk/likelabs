package com.redwerk.likelabs.application.messaging.exception;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SmsMessagingException extends RuntimeException {

	public static enum DeliveryStatus {
        GENERAL_ERROR, NETWORK_ERROR, REMOTE_ERROR, TIMEOUT_ERROR
	}
	
	private final DeliveryStatus deliveryStatus;

	private final String phone;
    
	public SmsMessagingException(DeliveryStatus status, String phone) {
		this(status, phone, null, null);		
	}

	public SmsMessagingException(DeliveryStatus status, String phone, String message) {
		this(status, phone, message, null);
	}	

	public SmsMessagingException(DeliveryStatus status, String phone, Throwable cause) {
		this(status, phone, null, cause);
	}

	public SmsMessagingException(DeliveryStatus status, String phone, String message, Throwable cause) {
		super(message, cause);
		this.deliveryStatus = status;
		this.phone = phone;
	}

	public DeliveryStatus getDeliveryStatus() {
		return deliveryStatus;
	}

	public String getPhone() {
		return phone;
	}
	
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("delivery status", deliveryStatus)
                .append("recipient phone", phone)
                .append("details", getMessage())
                .toString();
    }
}
