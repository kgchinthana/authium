package com.secure.authium.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthzenException extends  Exception{
    private final HttpStatus status;

    public AuthzenException(String message, HttpStatus status){
        super(message);
        this.status=status;
    }
}
