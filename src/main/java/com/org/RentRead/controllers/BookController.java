package com.org.RentRead.controllers;

import com.org.RentRead.dtos.BookRequestDto;
import com.org.RentRead.entities.Book;
import com.org.RentRead.services.Impl.BookServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@Slf4j
public class BookController {

    @Autowired
    private BookServiceImpl bookService;


    //End points accessible by authenticated users only :
    @GetMapping("/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable Long bookId){
        return ResponseEntity.ok().body(bookService.getBookById(bookId));
    }

    @GetMapping("/title")
    public ResponseEntity<Book> getBookByTitle(@RequestBody BookRequestDto bookDto){
        return ResponseEntity.ok().body(bookService.getBookByTitle(bookDto.getTitle()));
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }




    //End Points accessible by the ADMIN only :

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return new ResponseEntity<>(bookService.createBook(book), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{bookId}")
    public ResponseEntity<Book> updateBook(@PathVariable Long bookId, @RequestBody Book book){
        return new ResponseEntity<>(bookService.updateBook(bookId, book), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> removeBook(@PathVariable Long bookId){
        bookService.removeBook(bookId);
        return ResponseEntity.ok().body("The book with bookId "+bookId+" has been removed from the Library...!!!");
    }

}
