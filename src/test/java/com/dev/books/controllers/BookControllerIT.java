package com.dev.books.controllers;

import static com.dev.books.TestData.testBook;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.dev.books.TestData;
import com.dev.books.domain.Book;
import com.dev.books.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Test
    public void testThatBookIsCreated() throws Exception {
        final Book book = TestData.testBook();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String bookJson = objectMapper.writeValueAsString(book);
        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + book.getIsbn())
            .contentType(MediaType.APPLICATION_JSON)
            .content(bookJson))
        .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()));
    }

    @Test
    public void testThatRetrieveBookReturns404WhenBookNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/123123123"))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatRetrieveBookReturns200WhenBookExists() throws Exception {
        final Book book = testBook();
        bookService.create(book);
        mockMvc.perform(MockMvcRequestBuilders.get("/books/" + book.getIsbn()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()));
    }

    @Test
    public void testThatListBooksReturns200EmptyListWhenNoBooksExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void testThatListBooksReturns200AndBooksWhenBooksExist() throws Exception {
        final Book book = testBook();
        bookService.create(book);
        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].isbn").value(book.getIsbn()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].author").value(book.getAuthor()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value(book.getTitle()));
    }

    @Test
    public void testThatBookIsUpdated() throws Exception {
        final Book book = TestData.testBook();
        bookService.create(book);
        book.setAuthor("Olivia Jeans");
        final ObjectMapper objectMapper = new ObjectMapper();
        final String bookJson = objectMapper.writeValueAsString(book);
        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + book.getIsbn())
            .contentType(MediaType.APPLICATION_JSON)
            .content(bookJson))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()));
    }

    @Test
    public void testThatdeleteBookReturns204WhenBookDoesntExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/123123123"))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatdeleteBookReturns204WhenBookExist() throws Exception {
        final Book book = testBook();
        bookService.create(book);
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + book.getIsbn()))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
