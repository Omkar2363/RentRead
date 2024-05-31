package com.org.RentRead.services.Impl;

import com.org.RentRead.entities.Book;
import com.org.RentRead.entities.Rental;
import com.org.RentRead.entities.User;
import com.org.RentRead.exceptions.*;
import com.org.RentRead.repositories.BookRepository;
import com.org.RentRead.repositories.RentalRepository;
import com.org.RentRead.repositories.UserRepository;
import com.org.RentRead.services.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class RentalServiceImpl implements RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Rental rentBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found"));

        if (!book.isAvailable()) {
            throw new BookNotAvailableToRentException("Book is not available for rent");
        }

        // Check if the user has more than two active rentals
        long activeRentalsCount = rentalRepository.countByUserAndReturnDateIsNull(user);
        if (activeRentalsCount >= 2) {
            throw new UserHasTooManyActiveRentalsException("User already has two active rentals");
        }


        Rental rental = new Rental();
        rental.setUser(user);
        rental.setBook(book);
        rental.setRentalDate(LocalDate.now());

        book.setAvailable(false);
        bookRepository.save(book);

        return rentalRepository.save(rental);
    }


    @Override
    public Rental returnBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found"));

        Rental rental = rentalRepository.findByUserAndBookAndReturnDateIsNull(user, book)
                .orElseThrow(() -> new RentalNotFoundException("No rental record found for this book on your account for now."));


        rental.setReturnDate(LocalDate.now());
        rentalRepository.save(rental);

        book = rental.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        return rental;
    }


    public Long findUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        return user.getId();
    }
}

