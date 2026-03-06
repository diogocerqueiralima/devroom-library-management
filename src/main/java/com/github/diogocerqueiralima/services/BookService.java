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

    /**
     *
     * Retrieves a list of all books that are currently available for borrowing in the library management system.
     *
     * @return a list of all available books in the library management system
     */
    public List<Book> getAvailableBooks() {
        return bookRepository.findAll().stream()
                .filter(book -> book.getStatus() == BookStatus.AVAILABLE)
                .toList();
    }

    /**
     *
     * Retrieves a list of all books that are currently checked out by patrons in the library management system.
     *
     * @return a list of all checked out books in the library management system
     */
    public List<Book> getCheckedOutBooks() {
        return bookRepository.findAll().stream()
                .filter(book -> book.getStatus() == BookStatus.CHECKED_OUT)
                .toList();
    }

    /**
     *
     * Retrieves a list of all books written by the specified author in the library management system.
     *
     * @param author the name of the author whose books are to be retrieved
     * @return a list of all books written by the specified author in the library management system
     */
    public List<Book> getAllByAuthor(String author) {
        return bookRepository.findAll().stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .toList();
    }

    /**
     *
     * Retrieves a list of all books with the specified title in the library management system.
     *
     * @param title the title of the books to be retrieved
     * @return a list of all books with the specified title in the library management system
     */
    public List<Book> getAllByTitle(String title) {
        return bookRepository.findAll().stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .toList();
    }

}
