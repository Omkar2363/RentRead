package com.org.RentRead.exceptions;

public class EmailIdAlreadyUsedException extends RuntimeException {

    public EmailIdAlreadyUsedException(String message){
        super(message);
    }
}
