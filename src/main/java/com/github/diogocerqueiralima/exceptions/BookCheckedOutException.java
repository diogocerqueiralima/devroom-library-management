package com.github.diogocerqueiralima.exceptions;

import java.util.UUID;

/**
 * Infrastructure exception thrown when a book is currently checked out and cannot be checked out again.
 */
public class BookCheckedOutException extends RuntimeException {

    public BookCheckedOutException(UUID id) {
        super("Book with id " + id + " is currently checked out.");
    }

}
