package com.github.diogocerqueiralima.exceptions;

import java.util.UUID;

/**
 * Infrastructure exception thrown when a book is not found in the database.
 */
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(UUID id) {
        super("Book with id " + id + " not found.");
    }

}
