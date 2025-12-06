package com.oyku.SpringStarter.H2Test;

import com.oyku.SpringStarter.model.Book;
import com.oyku.SpringStarter.model.Library;
import com.oyku.SpringStarter.repository.BookRepository;
import com.oyku.SpringStarter.repository.LibraryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class LibraryH2Test {

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private BookRepository bookRepository;

    private Library library;
    private Book book;

    @BeforeEach
    void setup() {
        library = new Library();
        library.setName("Central Library");
        library.setLocation("Main Street");

        book = new Book();
        book.setTitle("Test Book");
        book.setLibrary(library);

        library.getBooks().add(book);
        library = libraryRepository.save(library);
    }

    @Test
    void testSaveAndFindById() {
        Optional<Library> found = libraryRepository.findById(library.getId());
        assertTrue(found.isPresent());
        assertEquals("Central Library", found.get().getName());
        assertEquals(1, found.get().getBooks().size()); // artık 1 olacak
        assertEquals("Test Book", found.get().getBooks().get(0).getTitle());
    }

    @Test
    void testUpdateLibrary() {
        library.setName("Updated Library");
        library.setLocation("Updated Street");
        libraryRepository.save(library);

        Library updated = libraryRepository.findById(library.getId()).orElseThrow();
        assertEquals("Updated Library", updated.getName());
        assertEquals("Updated Street", updated.getLocation());
    }

    @Test
    void testDeleteLibrary() {
        int libId = library.getId();
        libraryRepository.delete(library);

        assertFalse(libraryRepository.findById(libId).isPresent());

        Optional<Book> orphanBook = bookRepository.findById(book.getId());
        assertTrue(orphanBook.isEmpty(), "Book should be deleted when library is deleted");
    }

    @Test
    void testFindAllLibraries() {
        List<Library> libraries = libraryRepository.findAll();
        assertFalse(libraries.isEmpty());
        assertTrue(libraries.stream().anyMatch(lib -> lib.getName().equals("Central Library")));
    }
}

