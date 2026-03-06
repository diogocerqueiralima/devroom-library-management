package com.github.diogocerqueiralima.services;

import com.github.diogocerqueiralima.exceptions.BookCheckedOutException;
import com.github.diogocerqueiralima.exceptions.BookNotFoundException;
import com.github.diogocerqueiralima.exceptions.PatronNotFoundException;
import com.github.diogocerqueiralima.model.Book;
import com.github.diogocerqueiralima.model.BookStatus;
import com.github.diogocerqueiralima.model.Patron;
import com.github.diogocerqueiralima.repositories.BookRepository;
import com.github.diogocerqueiralima.repositories.PatronRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LibraryService {

    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;

    public LibraryService(BookRepository bookRepository, PatronRepository patronRepository) {
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
    }

    /**
     *
     * Borrows a book for a patron.
     *
     * @param patronId the ID of the patron who wants to borrow the book
     * @param bookId the ID of the book to be borrowed
     */
    public void borrow(UUID patronId, UUID bookId) {

        // 1. Get the patron and book from the repositories
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new PatronNotFoundException(patronId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        // 2. Check if the book is already checked out
        if (book.getStatus() == BookStatus.CHECKED_OUT) {
            throw new BookCheckedOutException(bookId);
        }

        // 3. Update the book's status to CHECKED_OUT and save it back to the repository
        book = new Book(book.getId(), book.getTitle(), book.getAuthor(), book.getYear(), BookStatus.CHECKED_OUT);
        bookRepository.save(book);

        // 4. Add the book to the patron's list of checked out books and save the patron back to the repository
        List<Book> checkedOutBooks = new ArrayList<>(patron.getCheckedOutBooks());
        checkedOutBooks.add(book);
        patron = new Patron(patron.getId(), patron.getName(), checkedOutBooks);
        patronRepository.save(patron);
    }

    /**
     *
     * Returns a book for a patron.
     *
     * @param patronId the ID of the patron who wants to return the book
     * @param bookId the ID of the book to be returned
     */
    public void returnBook(UUID patronId, UUID bookId) {

        // 1. Get the patron and book from the repositories
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new PatronNotFoundException(patronId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        // 2. Update the book's status to AVAILABLE and save it back to the repository
        book = new Book(book.getId(), book.getTitle(), book.getAuthor(), book.getYear(), BookStatus.AVAILABLE);
        bookRepository.save(book);

        // 3. Remove the book from the patron's list of checked out books and save the patron back to the repository
        List<Book> checkedOutBooks = new ArrayList<>(patron.getCheckedOutBooks());
        checkedOutBooks.removeIf(b -> b.getId().equals(bookId));
        patron = new Patron(patron.getId(), patron.getName(), checkedOutBooks);
        patronRepository.save(patron);
    }

}
