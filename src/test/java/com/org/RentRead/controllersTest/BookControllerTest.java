package com.org.RentRead.controllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.RentRead.controllers.BookController;
import com.org.RentRead.dtos.BookRequestDto;
import com.org.RentRead.entities.Book;
import com.org.RentRead.services.Impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookServiceImpl bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private Book book;
    private List<Book> bookList;

    @BeforeEach
    void setUp() {
        book = new Book(1L, "Book Title", "Author Name", "Genre", true);
        bookList = Arrays.asList(book, new Book(2L, "Another Book", "Another Author", "Another Genre", true));
    }

    @Test
    @WithMockUser
    void testGetBookById() throws Exception {
        when(bookService.getBookById(anyLong())).thenReturn(book);

        mockMvc.perform(get("/books/{bookId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.title").value(book.getTitle()))
                .andExpect(jsonPath("$.author").value(book.getAuthor()))
                .andExpect(jsonPath("$.genre").value(book.getGenre()))
                .andExpect(jsonPath("$.available").value(book.isAvailable()));

        verify(bookService, times(1)).getBookById(anyLong());
    }

    @Test
    @WithMockUser
    void testGetBookByTitle() throws Exception {
        when(bookService.getBookByTitle(anyString())).thenReturn(book);

        BookRequestDto bookRequestDto = new BookRequestDto("Book Title");

        mockMvc.perform(get("/books/title")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.title").value(book.getTitle()))
                .andExpect(jsonPath("$.author").value(book.getAuthor()))
                .andExpect(jsonPath("$.genre").value(book.getGenre()))
                .andExpect(jsonPath("$.available").value(book.isAvailable()));

        verify(bookService, times(1)).getBookByTitle(anyString());
    }

    @Test
    @WithMockUser
    void testGetAllBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(bookList);

        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(bookList.size()));

        verify(bookService, times(1)).getAllBooks();
    }

}