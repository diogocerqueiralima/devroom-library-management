package com.github.diogocerqueiralima.exceptions;

import com.github.diogocerqueiralima.model.Patron;

/**
 * Infrastructure exception thrown when a patron cannot be saved to the database.
 */
public class CouldNotSavePatronException extends RuntimeException {

    public CouldNotSavePatronException(Patron patron) {
        super("Could not save patron with id: " + patron.getId());
    }

}

