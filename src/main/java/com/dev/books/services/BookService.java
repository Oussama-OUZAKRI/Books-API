package com.dev.books.services;

import java.util.List;
import java.util.Optional;

import com.dev.books.domain.Book;

public interface BookService {

    boolean isBookExists(Book book);
    Book create(Book book);
    Optional<Book> findById(String isbn);
    List<Book> listBooks();
    void deleteBookById(String isbn);
}
