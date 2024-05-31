package com.org.RentRead.servicesTest;


import com.org.RentRead.entities.Book;
import com.org.RentRead.entities.Rental;
import com.org.RentRead.entities.Role;
import com.org.RentRead.entities.User;
import com.org.RentRead.exceptions.*;
import com.org.RentRead.repositories.BookRepository;
import com.org.RentRead.repositories.RentalRepository;
import com.org.RentRead.repositories.UserRepository;
import com.org.RentRead.services.Impl.RentalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RentalServiceImplTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private RentalServiceImpl rentalService;

    private User user;
    private Book book1, book2;
    private Rental rental;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User(1L, "Rashi@example.com", "abc", "Rashi", "Yadav", Role.USER);
        book1 = new Book(1L, "Book 1", "Author 1", "Genre 1", true);
        book2 = new Book(2L, "Book 2", "Author 2", "Genre 1", true);
        book1.setAvailable(true);
        book2.setAvailable(false);
        rental = new Rental(1L, user, book1, LocalDate.now(), null);
    }

    @Test
    void testRentBook() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(rentalRepository.countByUserAndReturnDateIsNull(user)).thenReturn(0L);
        when(rentalRepository.save(any(Rental.class))).thenReturn(rental);

        Rental rentedBook = rentalService.rentBook(1L, 1L);
        assertNotNull(rentedBook);
        assertEquals(user, rentedBook.getUser());
        assertEquals(book1, rentedBook.getBook());
        assertFalse(book1.isAvailable());
        verify(bookRepository, times(1)).save(book1);
    }

    @Test
    void testRentBookUserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> rentalService.rentBook(2L, 1L));
    }

    @Test
    void testRentBookBookNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> rentalService.rentBook(1L, 3L));
    }

    @Test
    void testRentBookBookNotAvailable() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book2));

        assertThrows(BookNotAvailableToRentException.class, () -> rentalService.rentBook(1L, 2L));
    }

    @Test
    void testRentBookUserHasTooManyActiveRentals() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(rentalRepository.countByUserAndReturnDateIsNull(user)).thenReturn(2L);

        assertThrows(UserHasTooManyActiveRentalsException.class, () -> rentalService.rentBook(1L, 1L));
    }

    @Test
    void testReturnBook() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(rentalRepository.findByUserAndBookAndReturnDateIsNull(user, book1)).thenReturn(Optional.of(rental));
        when(rentalRepository.save(any(Rental.class))).thenReturn(rental);

        Rental returnedBook = rentalService.returnBook(1L, 1L);
        assertNotNull(returnedBook.getReturnDate());
        assertTrue(book1.isAvailable());
        verify(bookRepository, times(1)).save(book1);
    }

    @Test
    void testReturnBookUserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> rentalService.returnBook(2L, 1L));
    }

    @Test
    void testReturnBookBookNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> rentalService.returnBook(1L, 3L));
    }

    @Test
    void testReturnBookRentalNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(rentalRepository.findByUserAndBookAndReturnDateIsNull(user, book1)).thenReturn(Optional.empty());

        assertThrows(RentalNotFoundException.class, () -> rentalService.returnBook(1L, 1L));
    }

    @Test
    void testFindUserIdByEmail() {
        when(userRepository.findByEmail("Rashi@example.com")).thenReturn(Optional.of(user));

        Long userId = rentalService.findUserIdByEmail("Rashi@example.com");
        assertEquals(user.getId(), userId);
    }

    @Test
    void testFindUserIdByEmailUserNotFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> rentalService.findUserIdByEmail("unknown@example.com"));
    }
}