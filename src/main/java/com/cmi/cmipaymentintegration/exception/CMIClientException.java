package com.cmi.cmipaymentintegration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class CMIClientException extends RuntimeException{
    public CMIClientException(String message){
        super(message);
    }
    public CMIClientException(String message,Throwable cause){
        super(message,cause);
    }
}
