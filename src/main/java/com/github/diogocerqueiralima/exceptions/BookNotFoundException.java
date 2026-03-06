package com.github.diogocerqueiralima.exceptions;

import java.util.UUID;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(UUID id) {
        super("Book with id " + id + " not found.");
    }

}
