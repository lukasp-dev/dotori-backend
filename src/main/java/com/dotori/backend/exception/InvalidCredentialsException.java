package com.dotori.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED) //401
public class InvalidCredentialsException extends ApiException {
    public InvalidCredentialsException() {
        super("Invalid Email or Password.");
    }
}
