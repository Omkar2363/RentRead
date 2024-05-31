package com.org.RentRead.exceptions;

public class UserNotFoundException extends  RuntimeException {

    public UserNotFoundException(String message){
        super(message);
    }
}
