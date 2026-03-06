package com.github.diogocerqueiralima.exceptions;

import java.util.UUID;

/**
 * Infrastructure exception thrown when a patron cannot be deleted from the database.
 */
public class CouldNotDeletePatronException extends RuntimeException {

    public CouldNotDeletePatronException(UUID id) {
        super("Could not delete patron with id: " + id);
    }

}

