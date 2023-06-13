package com.dev.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dev.backend.controller.BookController;
import com.dev.backend.exception.ResourceNotFoundException;
import com.dev.backend.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;
    
    @MockBean
    BookRepository bookRepository;

    Book book1 = new Book("book1", "author1", "genre1", 1);
    Book book2 = new Book("book2", "author2", "genre2", 2);
    Book book3= new Book("book3", "author1", "genre1", 3);

    @Test
    public void getAllBooks_success() throws Exception {
        List<Book> books = new ArrayList<>(Arrays.asList(book1, book2, book3));
        
        Mockito.when(bookRepository.findAll()).thenReturn(books);
        
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].title", is(book1.getTitle())))
                .andExpect(jsonPath("$[1].title", is(book2.getTitle())))
                .andExpect(jsonPath("$[2].title", is(book3.getTitle())));
    }

    @Test
    public void getBookById_success() throws Exception {      
        Mockito.when(bookRepository.findById(book1.getId())).thenReturn(Optional.of(book1));
        
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/books/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.title", is("book1")));
    }

    @Test
    public void getBookById_notFound() throws Exception {      
        String exceptionParam = "not_found";

        mockMvc.perform(MockMvcRequestBuilders
        .get("/api/books/1", exceptionParam)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
        .andExpect(result -> assertEquals("Book not found for this id :: 1", result.getResolvedException().getMessage()));
    }

    @Test
    public void createBook_success() throws Exception {
        Mockito.when(bookRepository.save(book1)).thenReturn(book1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/books")
                .content(asJsonString(book1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("book1")));
    }

    @Test
    public void updateBook_success() throws Exception {

        Book updatedBook = new Book("updatedBook1", "updatedAuthor1", "updatedGenre1", 3);

        Mockito.when(bookRepository.findById(book1.getId())).thenReturn(Optional.of(book1));
        Mockito.when(bookRepository.save(updatedBook)).thenReturn(updatedBook);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/books/{id}", 0)
                .content(asJsonString(updatedBook))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("updatedBook1")));
    }

    @Test
    public void deleteBook_success() throws Exception {

        Mockito.when(bookRepository.findById(book1.getId())).thenReturn(Optional.of(book1));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/books/{id}", 0))
                    .andExpect(status().isAccepted());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
