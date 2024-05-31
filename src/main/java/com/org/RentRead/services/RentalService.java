package com.org.RentRead.services;

import com.org.RentRead.entities.Rental;

public interface RentalService {

    Rental rentBook(Long userId, Long bookId);
    Rental returnBook(Long userId, Long bookId);

    Long findUserIdByEmail(String email);
}
