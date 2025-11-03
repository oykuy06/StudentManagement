package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.model.Library;
import com.oyku.SpringStarter.repository.LibraryRepository;
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

    public Optional<Library> getLibraryById(int id) {
        return libraryRepository.findById(id);
    }

    public Library addNewLibrary(Library library) {
        return libraryRepository.save(library);
    }

    public Library updateLibrary(Library library){
        return libraryRepository.save(library);
    }

    public boolean deleteLibrary(int id) {
        if(!libraryRepository.existsById(id)) return false;
        libraryRepository.deleteById(id);
        return true;
    }
}
