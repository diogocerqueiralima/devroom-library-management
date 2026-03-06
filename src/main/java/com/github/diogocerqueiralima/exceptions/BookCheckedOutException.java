package com.github.diogocerqueiralima.exceptions;

import java.util.UUID;

public class BookCheckedOutException extends RuntimeException {

    public BookCheckedOutException(UUID id) {
        super("Book with id " + id + " is currently checked out.");
    }

}
