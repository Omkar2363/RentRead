package com.org.RentRead.exceptions;

public class UserHasTooManyActiveRentalsException extends RuntimeException {

    public UserHasTooManyActiveRentalsException(String message){
        super(message);
    }

}
