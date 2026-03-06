package com.github.diogocerqueiralima.exceptions;

import java.util.UUID;

/**
 * Infrastructure exception thrown when patrons cannot be retrieved from the database.
 */
public class CouldNotRetrievePatronException extends RuntimeException {

    public CouldNotRetrievePatronException() {
        super("Could not retrieve patrons from the database.");
    }

    public CouldNotRetrievePatronException(UUID id) {
        super("Could not retrieve patron with id: " + id);
    }

}

