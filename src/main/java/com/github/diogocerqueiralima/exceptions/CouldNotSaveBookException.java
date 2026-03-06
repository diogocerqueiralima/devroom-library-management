package com.github.diogocerqueiralima.exceptions;

import com.github.diogocerqueiralima.model.Book;

/**
 * Infrastructure exception thrown when a book cannot be saved to the database.
 */
public class CouldNotSaveBookException extends RuntimeException {

    public CouldNotSaveBookException(Book book) {
        super("Could not save book with id: " + book.getId());
    }

}
