package com.org.RentRead.services;

import com.org.RentRead.entities.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book getBookById(Long bookId);
    Book getBookByTitle(String bookTitle);
    List<Book> getAllBooks();
    Book createBook(Book book);
    Book updateBook(Long bookId, Book book);
    void removeBook(Long bookId);

}
