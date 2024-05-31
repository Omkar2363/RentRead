package com.org.RentRead.exceptions;

public class PasswordMisMatchException extends RuntimeException {

    public PasswordMisMatchException(String message){
        super(message);
    }
}
