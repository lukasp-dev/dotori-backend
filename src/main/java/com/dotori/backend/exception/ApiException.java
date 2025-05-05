package com.dotori.backend.exception;

public abstract class ApiException extends RuntimeException {
    public ApiException(String message){
        super(message);
    }
}
