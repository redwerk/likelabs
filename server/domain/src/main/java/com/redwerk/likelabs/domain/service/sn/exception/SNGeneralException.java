package com.redwerk.likelabs.domain.service.sn.exception;

public class SNGeneralException extends RuntimeException {

    public SNGeneralException(Throwable cause) {
        super(cause);
    }

    public SNGeneralException(String message) {
    	super(message);
    }
    
    public SNGeneralException() {
    }
}
