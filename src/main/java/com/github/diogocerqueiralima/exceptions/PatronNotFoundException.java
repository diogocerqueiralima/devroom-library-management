package com.github.diogocerqueiralima.exceptions;

import java.util.UUID;

/**
 * Infrastructure exception thrown when a patron is not found in the database.
 */
public class PatronNotFoundException extends RuntimeException {

    public PatronNotFoundException(UUID id) {
        super("Patron with id " + id + " not found.");
    }

}
