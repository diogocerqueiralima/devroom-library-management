package com.github.diogocerqueiralima.services;

import com.github.diogocerqueiralima.exceptions.PatronNotFoundException;
import com.github.diogocerqueiralima.model.Patron;
import com.github.diogocerqueiralima.repositories.PatronRepository;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

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

    public void delete(UUID id) {
        patronRepository.delete(id);
    }

    public List<Patron> getAll() {
        return patronRepository.findAll();
    }

}
