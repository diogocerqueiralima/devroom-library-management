package com.github.diogocerqueiralima.services;

import com.github.diogocerqueiralima.exceptions.PatronNotFoundException;
import com.github.diogocerqueiralima.model.Patron;
import com.github.diogocerqueiralima.repositories.PatronRepository;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Service class responsible for managing patrons in the library management system.
 * @see Patron
 * @see PatronRepository
 */
public class PatronService {

    private final PatronRepository patronRepository;

    public PatronService(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    public Patron add(String name) {
        // 1. Create a new patron with a unique ID and the provided name, and an empty list of checked out books
        Patron patron = new Patron(UUID.randomUUID(), name, Collections.emptyList());
        patronRepository.save(patron);
        return patron;
    }

    public void update(UUID id, String name) {

        // 1. Retrieve the existing patron from the repository using the provided ID
        Patron patron = patronRepository.findById(id)
                .orElseThrow(() -> new PatronNotFoundException(id));

        // 2. Create a new Patron object with the updated name while preserving the existing list of checked out books
        patron = new Patron(id, name, patron.getCheckedOutBooks());
        patronRepository.save(patron);
    }

    /**
     *
     * Deletes the patron with the specified unique identifier from the library management system.
     * If the patron with the given ID does not exist, it will be ignored and no action will be taken.
     *
     * @param id the unique identifier of the patron to be deleted
     */
    public void delete(UUID id) {
        patronRepository.delete(id);
    }

    /**
     * @return a list of all patrons in the library management system. If there are no patrons, an empty list will be returned.
     */
    public List<Patron> getAll() {
        return patronRepository.findAll();
    }

}
