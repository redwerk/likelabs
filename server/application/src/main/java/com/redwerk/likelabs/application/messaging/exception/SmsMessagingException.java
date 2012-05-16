package com.redwerk.likelabs.application.messaging.exception;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SmsMessagingException extends GeneralMessagingException {

	public static enum DeliveryStatus {
		PENDING, DELIVERY_FAIL, NETWORK_ERROR, GENERAL_ERROR, REMOTE_ERROR, TIMEOUT
	}
	
	private final DeliveryStatus deliveryStatus;
	private final String telephoneNumber;
    
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
		this.telephoneNumber = phone;
	}

	public DeliveryStatus getDeliveryStatus() {
		return deliveryStatus;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("delivery status", deliveryStatus)
                .append("recipient phone", telephoneNumber)
                .append("details", getMessage())
                .toString();
    }
}
