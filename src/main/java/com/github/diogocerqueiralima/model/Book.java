package com.github.diogocerqueiralima.model;

import java.util.UUID;

/**
 * Represents a book in the library management system.
 *
 * @see BookStatus
 */
public class Book {

    private final UUID id;
    private final String title;
    private final String author;
    private final String year;
    private final BookStatus status;

    public Book(UUID id, String title, String author, String year, BookStatus status) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getYear() {
        return year;
    }

    public BookStatus getStatus() {
        return status;
    }

}
