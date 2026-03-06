package com.github.diogocerqueiralima.repositories;

import com.github.diogocerqueiralima.exceptions.CouldNotDeleteBookException;
import com.github.diogocerqueiralima.exceptions.CouldNotRetrieveBookException;
import com.github.diogocerqueiralima.exceptions.CouldNotSaveBookException;
import com.github.diogocerqueiralima.model.Book;
import com.github.diogocerqueiralima.model.BookStatus;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents the repository for managing books in the library management system.
 *
 * @see Book
 */
public class BookRepository {

    private static final String[] HEADERS = {"id", "title", "author", "year", "status"};
    private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT
            .withHeader("id", "title", "author", "year", "status")
            .withFirstRecordAsHeader()
            .withTrim();

    private final String filename;

    /**
     *
     * Initializes a new instance of the {@link BookRepository} class with the specified filename for storing book data.
     *
     * @param filename the name of the file where book data will be stored
     */
    public BookRepository(String filename) {
        this.filename = filename;
    }

    public void save(Book book) {

        // 1. Get all existing books from the repository
        List<Book> books = new ArrayList<>(findAll());

        // 2. Remove the book with the same ID if it already exists in the repository
        books.removeIf(b -> b.getId().equals(book.getId()));
        books.add(book);

        // 3. Write the updated list of books back to the file
        try (
                FileWriter fileWriter = new FileWriter(this.filename, false);
                CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSV_FORMAT)
        ) {

            csvPrinter.printRecord((Object[]) HEADERS);

            for (Book b : books) {
                csvPrinter.printRecord(
                        b.getId(),
                        b.getTitle(),
                        b.getAuthor(),
                        b.getYear(),
                        b.getStatus()
                );
            }

            csvPrinter.flush();

        } catch (Exception e) {
            e.printStackTrace();
            throw new CouldNotSaveBookException(book);
        }

    }

    /**
     *
     * Retrieves all books from the repository.
     * This method reads the book data from the specified file and returns a list of {@link Book} objects.
     *
     * @return a list of all books in the repository
     * @throws CouldNotRetrieveBookException if there is an error while reading the book data from the file
     */
    public List<Book> findAll() {

        // 1. Read the book data from the file and convert it to a list of Book objects
        try (
                FileReader fileReader = new FileReader(this.filename);
                CSVParser csvParser = CSV_FORMAT.parse(fileReader)
        ) {

            // 2. Convert each record in the CSV file to a Book object and return the list of books
            return csvParser.stream()
                    .map(record -> new Book(
                            UUID.fromString(record.get("id")),
                            record.get("title"),
                            record.get("author"),
                            record.get("year"),
                            BookStatus.valueOf(record.get("status"))
                    ))
                    .toList();

        } catch (Exception e) {
            throw new CouldNotRetrieveBookException();
        }
    }

    /**
     *
     * Deletes a book with the specified ID from the repository.
     * If a book with the specified ID does not exist in the repository, no action will be taken.
     *
     * @param id the unique identifier of the book to be deleted
     * @throws CouldNotDeleteBookException if there is an error while deleting the book with the specified ID from the repository
     */
    public void delete(UUID id) {

        // 1. Get all existing books from the repository
        List<Book> books = new ArrayList<>(findAll());

        // 2. Remove the book with the specified ID from the list of books
        books.removeIf(b -> b.getId().equals(id));

        // 3. Write the updated list of books back to the file
        try (
                FileWriter fileWriter = new FileWriter(this.filename, false);
                CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSV_FORMAT)
        ) {

            csvPrinter.printRecord((Object[]) HEADERS);

            for (Book b : books) {
                csvPrinter.printRecord(
                        b.getId(),
                        b.getTitle(),
                        b.getAuthor(),
                        b.getYear(),
                        b.getStatus()
                );
            }

        } catch (Exception e) {
            throw new CouldNotDeleteBookException(id);
        }

    }

    /**
     *
     * Retrieves a book with the specified unique identifier from the repository.
     *
     * @param id the unique identifier of the book to be retrieved
     * @return an Optional containing the {@link Book} with the specified unique identifier if it exists in the repository,
     * or an empty Optional if no such book exists
     */
    public Optional<Book> findById(UUID id) {
        return findAll().stream()
                .filter(book -> book.getId().equals(id))
                .findFirst();
    }

}
