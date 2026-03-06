package com.github.diogocerqueiralima.exceptions;

import java.util.UUID;

/**
 * Infrastructure exception thrown when books cannot be retrieved from the database.
 */
public class CouldNotRetrieveBookException extends RuntimeException {

    public CouldNotRetrieveBookException() {
        super("Could not retrieve books from the database.");
    }

    public CouldNotRetrieveBookException(UUID id) {
        super("Could not retrieve book with id: " + id);
    }

}
