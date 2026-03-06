package com.github.diogocerqueiralima.model;

import java.util.List;
import java.util.UUID;

/**
 * Represents a patron in the library management system.
 */
public class Patron {

    private final UUID id;
    private final String name;
    private final List<Book> checkedOutBooks;

    public Patron(UUID id, String name, List<Book> checkedOutBooks) {
        this.id = id;
        this.name = name;
        this.checkedOutBooks = checkedOutBooks;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Book> getCheckedOutBooks() {
        return checkedOutBooks;
    }

}
