package com.dotori.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) //409
public class EmailAlreadyExistsException extends ApiException {
    public EmailAlreadyExistsException() {
        super("This email is already in use.");
    }
}
