package com.redwerk.likelabs.application.sn.exception;

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
