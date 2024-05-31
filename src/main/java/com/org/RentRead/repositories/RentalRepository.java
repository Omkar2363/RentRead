package com.org.RentRead.repositories;

import com.org.RentRead.entities.Book;
import com.org.RentRead.entities.Rental;
import com.org.RentRead.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUserAndReturnDateIsNull(User user);
    Long countByUserAndReturnDateIsNull(User user);

    Optional<Rental> findByUserAndBookAndReturnDateIsNull(User user, Book book);
}
