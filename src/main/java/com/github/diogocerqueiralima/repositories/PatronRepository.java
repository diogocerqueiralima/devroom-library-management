package com.github.diogocerqueiralima.repositories;

import com.github.diogocerqueiralima.exceptions.CouldNotDeletePatronException;
import com.github.diogocerqueiralima.exceptions.CouldNotRetrievePatronException;
import com.github.diogocerqueiralima.exceptions.CouldNotSavePatronException;
import com.github.diogocerqueiralima.model.Book;
import com.github.diogocerqueiralima.model.Patron;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Represents the repository for managing patrons in the library management system.
 *
 * @see Patron
 */
public class PatronRepository {

    private static final String[] HEADERS = {"id", "name", "checked_out_books"};
    private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT
            .withHeader("id", "name", "checked_out_books")
            .withFirstRecordAsHeader()
            .withTrim();

    private final String filename;
    private final BookRepository bookRepository;

    /**
     *
     * Initializes a new instance of the {@link PatronRepository} class with the specified filename for storing patron data.
     *
     * @param filename the name of the file where patron data will be stored
     */
    public PatronRepository(String filename, BookRepository bookRepository) {
        this.filename = filename;
        this.bookRepository = bookRepository;
    }

    /**
     *
     * Saves the specified patron to the repository.
     * If a patron with the same unique identifier already exists, it will be updated with the new information.
     *
     * @param patron the patron to be saved or updated in the repository
     */
    public void save(Patron patron) {

        // 1. Get all existing patrons from the repository
        List<Patron> patrons = new ArrayList<>(findAll());

        // 2. Remove any existing patron with the same unique identifier as the one being saved
        patrons.removeIf(p -> p.getId().equals(patron.getId()));
        patrons.add(patron);

        // 3. Write the updated list of patrons back to the CSV file
        try (
                FileWriter fileWriter = new FileWriter(this.filename, false);
                CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSV_FORMAT)
        ) {

            csvPrinter.printRecord((Object[]) HEADERS);

            for (Patron p : patrons) {
                csvPrinter.printRecord(
                        p.getId(),
                        p.getName(),
                        serializeCheckedOutBooks(p.getCheckedOutBooks())
                );
            }

            csvPrinter.flush();

        } catch (Exception e) {
            throw new CouldNotSavePatronException(patron);
        }

    }

    /**
     *
     * Gets a list of all patrons currently stored in the repository.
     *
     * @return a list of all patrons in the repository
     */
    public List<Patron> findAll() {

        // 1. Read the patron data from the CSV file and parse it into a list of Patron objects
        try (
                FileReader fileReader = new FileReader(this.filename);
                CSVParser csvParser = CSV_FORMAT.parse(fileReader)
        ) {

            // 2. Map each record in the CSV file to a Patron object and return the list of patrons
            return csvParser.stream()
                    .map(record -> new Patron(
                            UUID.fromString(record.get("id")),
                            record.get("name"),
                            deserializeCheckedOutBooks(record.get("checked_out_books"))
                    ))
                    .toList();

        } catch (Exception e) {
            throw new CouldNotRetrievePatronException();
        }

    }

    /**
     *
     * Deletes the patron with the specified unique identifier from the repository.
     * If a patron with the specified unique identifier does not exist, no action will be taken.
     *
     * @param id the unique identifier of the patron to be deleted
     */
    public void delete(UUID id) {

        // 1. Get all existing patrons from the repository
        List<Patron> patrons = new ArrayList<>(findAll());

        // 2. Remove any existing patron with the specified unique identifier
        patrons.removeIf(p -> p.getId().equals(id));

        // 3. Write the updated list of patrons back to the CSV file
        try (
                FileWriter fileWriter = new FileWriter(this.filename, false);
                CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSV_FORMAT)
        ) {

            csvPrinter.printRecord((Object[]) HEADERS);

            for (Patron p : patrons) {
                csvPrinter.printRecord(
                        p.getId(),
                        p.getName(),
                        serializeCheckedOutBooks(p.getCheckedOutBooks())
                );
            }

            csvPrinter.flush();

        } catch (Exception e) {
            throw new CouldNotDeletePatronException(id);
        }

    }

    /**
     *
     * Retrieves a patron with the specified unique identifier from the repository.
     *
     * @param id the unique identifier of the patron to be retrieved
     * @return an Optional containing the {@link Patron} with the specified unique identifier, or an empty Optional if no such patron exists in the repository
     */
    public Optional<Patron> findById(UUID id) {
        return findAll().stream()
                .filter(patron -> patron.getId().equals(id))
                .findFirst();
    }

    /**
     *
     * Serializes a list of checked out books into a string format suitable for storage in the CSV file.
     *
     * @param checkedOutBooks the list of checked out books to be serialized
     * @return a string representation of the list of checked out books, where each book's unique identifier is separated by a semicolon (;).
     * If the list is null or empty, an empty string will be returned.
     */
    private String serializeCheckedOutBooks(List<Book> checkedOutBooks) {

        // 1. If the list of checked out books is null or empty, return an empty string
        if (checkedOutBooks == null || checkedOutBooks.isEmpty()) {
            return "";
        }

        // 2. Convert the list of checked out books into a string format where each book's unique identifier is separated by a semicolon (;)
        return checkedOutBooks.stream()
                .map(book -> book.getId().toString())
                .collect(Collectors.joining(";"));
    }

    /**
     *
     * Deserializes a string representation of checked out books from the CSV file into a list of {@link Book} objects.
     *
     * @param rawCheckedOutBooks the string representation of checked out books, where each book's unique identifier is separated by a semicolon (;).
     * @return a list of {@link Book} objects corresponding to the unique identifiers in the input string.
     * If the input string is null or blank, an empty list will be returned.
     */
    private List<Book> deserializeCheckedOutBooks(String rawCheckedOutBooks) {

        // 1. If the input string is null or blank, return an empty list
        if (rawCheckedOutBooks == null || rawCheckedOutBooks.isBlank()) {
            return Collections.emptyList();
        }

        // 2. Split the input string by semicolons, trim any whitespace, convert each unique identifier to a UUID,
        // retrieve the corresponding Book from the book repository, and return the list of Book objects
        return Arrays.stream(rawCheckedOutBooks.split(";"))
                .filter(value -> !value.isBlank())
                .map(String::trim)
                .map(UUID::fromString)
                .map(bookRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

}
