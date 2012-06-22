package com.redwerk.likelabs.domain.service.sn.exception;

public class SNException extends RuntimeException {

    public SNException(Throwable cause) {
        super(cause);
    }

    public SNException(String message) {
    	super(message);
    }
    
    public SNException() {
    }
}
