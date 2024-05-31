package com.org.RentRead.servicesTest;

import com.org.RentRead.entities.Book;
import com.org.RentRead.exceptions.BookNotFoundException;
import com.org.RentRead.repositories.BookRepository;
import com.org.RentRead.services.Impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book1, book2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book1 = new Book(1L, "Book 1", "Author 1", "Genre 1", true);
        book2 = new Book(2L, "Book 2", "Author 2", "Genre 2", true);
    }

    @Test
    void testGetBookById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        Book foundBook = bookService.getBookById(1L);
        assertEquals(book1, foundBook);
    }

    @Test
    void testGetBookByIdNotFound() {
        when(bookRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(3L));
    }

    @Test
    void testGetBookByTitle() {
        when(bookRepository.findByTitle("Book 1")).thenReturn(Optional.of(book1));
        Book foundBook = bookService.getBookByTitle("Book 1");
        assertEquals(book1, foundBook);
    }

    @Test
    void testGetBookByTitleNotFound() {
        when(bookRepository.findByTitle("Non-existent Book")).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.getBookByTitle("Non-existent Book"));
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        when(bookRepository.findAll()).thenReturn(books);
        List<Book> foundBooks = bookService.getAllBooks();
        assertEquals(books, foundBooks);
    }

    @Test
    void testCreateBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(book1);
        Book savedBook = bookService.createBook(book1);
        assertEquals(book1, savedBook);
    }

    @Test
    void testUpdateBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookRepository.save(any(Book.class))).thenReturn(book2);
        Book updatedBook = bookService.updateBook(1L, book2);
        assertEquals(book2, updatedBook);
    }

    @Test
    void testUpdateBookNotFound() {
        when(bookRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(3L, book2));
    }

    @Test
    void testRemoveBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        bookService.removeBook(1L);
        verify(bookRepository, times(1)).delete(book1);
    }

    @Test
    void testRemoveBookNotFound() {
        when(bookRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.removeBook(3L));
    }
}