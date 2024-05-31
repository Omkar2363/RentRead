package com.org.RentRead.services.Impl;

import com.org.RentRead.entities.Book;
import com.org.RentRead.exceptions.BookNotFoundException;
import com.org.RentRead.repositories.BookRepository;
import com.org.RentRead.services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book getBookById(Long bookId) {
        log.info("Fetching book with bookId : "+bookId);
        Book book = bookRepository.findById(bookId)
                                .orElseThrow(() -> new BookNotFoundException("Book with bookId "+bookId+" not found in the library...!!!"));
        log.info("Book fetched successfully from the library");
        return book;
    }

    public Book getBookByTitle(String bookTitle) {
        log.info("Fetching book with bookTitle : "+bookTitle);
        Book book = bookRepository.findByTitle(bookTitle)
                .orElseThrow(() -> new BookNotFoundException("Book with bookTitle '"+bookTitle+"' not found in the library...!!!"));
        log.info("Book fetched successfully from the library");
        return book;
    }

    public List<Book> getAllBooks() {
        log.info("Fetching all the books available in the library...!!!");
        return bookRepository.findAll();
    }

    public Book createBook(Book book) {
        log.info("Adding a new book to the library...!!!");
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Long bookId, Book updateBook) {
        log.info("Updating the book...");
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new BookNotFoundException("Book with given bookId to update does not exist in the library stock."));

        updateBook.setId(book.getId());

        //Save the new book :
        Book updatedBook = bookRepository.save(updateBook);
        log.info("Book has been updated successfully...!!!");
        return updatedBook;
    }

    @Override
    public void removeBook(Long bookId) {
        log.info("Fetching the book to remove from the library...");
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with bookId "+bookId+" not found in the library...!!!"));

        bookRepository.delete(book);
        log.info("Book with bookId "+bookId+" has been removed from the library...!!!");
    }


}

