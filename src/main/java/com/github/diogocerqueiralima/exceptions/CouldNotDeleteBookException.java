package com.github.diogocerqueiralima.exceptions;

import java.util.UUID;

/**
 * Infrastructure exception thrown when a book cannot be deleted from the database.
 */
public class CouldNotDeleteBookException extends RuntimeException {

    public CouldNotDeleteBookException(UUID id) {
        super("Could not delete book with id: " + id);
    }

}
