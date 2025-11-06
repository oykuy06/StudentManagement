package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.model.Library;
import com.oyku.SpringStarter.repository.LibraryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {

    private final LibraryRepository libraryRepository;

    public LibraryService(LibraryRepository libraryRepository){
        this.libraryRepository = libraryRepository;
    }

    public List<Library> getAllLibraries() {
        return libraryRepository.findAll();
    }

    public Library getLibraryById(int id) {
        return libraryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Library not found with ID: " + id));
    }

    public Library addNewLibrary(Library library) {
        return libraryRepository.save(library);
    }

    public Library updateLibrary(int id, Library updatedData){
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Library not found with ID: " + id));

        library.setName(updatedData.getName());
        library.setLocation(updatedData.getLocation());

        return libraryRepository.save(library);
    }

    public void deleteLibrary(int id) {
        if (!libraryRepository.existsById(id)) {
            throw new EntityNotFoundException("Library not found with ID: " + id);
        }
        libraryRepository.deleteById(id);
    }

}
