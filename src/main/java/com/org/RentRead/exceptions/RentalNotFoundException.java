package com.org.RentRead.exceptions;

public class RentalNotFoundException extends RuntimeException {

    public RentalNotFoundException(String message){
        super(message);
    }
}
