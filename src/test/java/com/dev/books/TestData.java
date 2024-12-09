package com.dev.books;

import com.dev.books.domain.Book;
import com.dev.books.domain.BookEntity;

public class TestData {
    private TestData(){}

    public static Book testBook() {
        return Book.builder()
            .isbn("01925736")
            .author("Ahmed Sefrioui")
            .title("La boîte à Merveilles")
            .build();
    }

    public static BookEntity testBookEntity() {
        return BookEntity.builder()
            .isbn("01925736")
            .author("Ahmed Sefrioui")
            .title("La boîte à Merveilles")
            .build();
    }

}
