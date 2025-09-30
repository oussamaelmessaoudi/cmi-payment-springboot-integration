package com.cmi.cmipaymentintegration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class CMIServerException extends RuntimeException{
    public CMIServerException(String message){
        super(message);
    }

    public CMIServerException(String message,Throwable cause){
        super(message,cause);
    }
}
