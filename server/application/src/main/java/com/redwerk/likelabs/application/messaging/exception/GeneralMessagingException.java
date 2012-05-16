package com.redwerk.likelabs.application.messaging.exception;

public class GeneralMessagingException extends RuntimeException {
	
	public GeneralMessagingException() {
		super();
	}

	public GeneralMessagingException(String message) {
		super(message);
	}	
	
    public GeneralMessagingException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeneralMessagingException(Throwable cause) {
        super(cause);
    }
}
