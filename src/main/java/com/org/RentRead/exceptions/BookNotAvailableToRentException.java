package com.org.RentRead.exceptions;


public class BookNotAvailableToRentException extends RuntimeException{

    public BookNotAvailableToRentException(String message){
        super(message);
    }
}
