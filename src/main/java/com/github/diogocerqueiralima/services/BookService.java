package com.github.diogocerqueiralima.services;

import com.github.diogocerqueiralima.exceptions.BookNotFoundException;
import com.github.diogocerqueiralima.model.Book;
import com.github.diogocerqueiralima.model.BookStatus;
import com.github.diogocerqueiralima.repositories.BookRepository;

import java.util.List;
import java.util.UUID;

/**
 * Service class responsible for managing books in the library management system.
 * It provides methods to add, delete, and retrieve books.
 *
 * @see Book
 * @see BookRepository
 */
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     *
     * Adds a new book to the library management system with the specified title, author, and year.
     * The book will be assigned a unique identifier and will have its status set to AVAILABLE by default.
     *
     * @param title the title of the book to be added
     * @param author the author of the book to be added
     * @param year the publication year of the book to be added
     */
    public void add(String title, String author, String year) {
        Book book = new Book(UUID.randomUUID(), title, author, year, BookStatus.AVAILABLE);
        bookRepository.save(book);
    }

    public void update(UUID id, String title, String author, String year) {

        // 1. Retrieve the existing book from the repository using the provided ID
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        // 2. Create a new Book object with the updated information while preserving the existing status
        book = new Book(id, title, author, year, book.getStatus());
        bookRepository.save(book);
    }

    /**
     *
     * Deletes the book with the specified unique identifier from the library management system.
     *
     * @param id the unique identifier of the book to be deleted
     */
    public void delete(UUID id) {
        bookRepository.delete(id);
    }

    /**
     *
     * Retrieves a list of all books currently stored in the library management system.
     *
     * @return a list of all books in the library management system
     */
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

}
