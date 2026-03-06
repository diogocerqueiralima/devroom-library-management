package com.github.diogocerqueiralima.exceptions;

import java.util.UUID;

public class PatronNotFoundException extends RuntimeException {

    public PatronNotFoundException(UUID id) {
        super("Patron with id " + id + " not found.");
    }

}
