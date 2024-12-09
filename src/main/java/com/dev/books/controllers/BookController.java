package com.dev.books.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.dev.books.domain.Book;
import com.dev.books.services.BookService;

@Controller
public class BookController {
    final private BookService bookService;

    @Autowired
    public BookController(final BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(path = "/books/{isbn}")
    public ResponseEntity<Book> createBook(
        @PathVariable final String isbn,
        @RequestBody final Book book) {
            book.setIsbn(isbn);
            final Book savedBook = bookService.create(book);
            final ResponseEntity<Book> response = new ResponseEntity<Book>(savedBook, HttpStatus.CREATED);
            return response;
    }

    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<Book> retrieveBook(@PathVariable final String isbn) {
        final Optional<Book> foundBook = bookService.findById(isbn);
        return foundBook.map(book -> new ResponseEntity<Book>(book, HttpStatus.OK))
            .orElse(new ResponseEntity<Book>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/books")
    public ResponseEntity<List<Book>> listBooks() {
        return new ResponseEntity<List<Book>>(bookService.listBooks(), HttpStatus.OK);
    }

    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<Book> updateBook(
        @PathVariable final String isbn,
        @RequestBody final Book book) {
            book.setIsbn(isbn);
            final boolean isBookExists = bookService.isBookExists(book);
            final Book savedBook = bookService.create(book);
            if (isBookExists) return new ResponseEntity<Book>(savedBook, HttpStatus.OK);
            else return new ResponseEntity<Book>(savedBook, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity deleteBook(@PathVariable final String isbn) {
        bookService.deleteBookById(isbn);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } 
    
}
