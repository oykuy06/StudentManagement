package com.oyku.SpringStarter.repository;

import com.oyku.SpringStarter.model.Book;
import com.oyku.SpringStarter.model.Library;
import com.oyku.SpringStarter.model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    @Test
    void saveBook_shouldPersistCorrectly() {
        Student student = new Student();
        student.setName("Oyku");
        studentRepository.save(student);

        Library library = new Library();
        library.setName("Main Library");
        libraryRepository.save(library);

        Book book = new Book();
        book.setTitle("Test Book");
        book.setStudent(student);
        book.setLibrary(library);

        Book saved = bookRepository.save(book);

        assertNotNull(saved.getId());
        assertEquals("Test Book", saved.getTitle());
        assertEquals(student.getId(), saved.getStudent().getId());
    }

    @Test
    void findById_shouldReturnBook() {
        Student student = new Student();
        student.setName("Oyku");
        studentRepository.save(student);

        Library library = new Library();
        library.setName("Main Library");
        libraryRepository.save(library);

        Book book = new Book();
        book.setTitle("My Book");
        book.setStudent(student);
        book.setLibrary(library);

        Book saved = bookRepository.save(book);

        Optional<Book> found = bookRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("My Book", found.get().getTitle());
    }

    @Test
    void findAll_shouldReturnList() {
        Student student = new Student();
        student.setName("Oyku");
        studentRepository.save(student);

        Library library = new Library();
        library.setName("Main Library");
        libraryRepository.save(library);

        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setStudent(student);
        book1.setLibrary(library);

        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setStudent(student);
        book2.setLibrary(library);

        bookRepository.save(book1);
        bookRepository.save(book2);

        List<Book> all = bookRepository.findAll();

        assertEquals(2, all.size());
    }
}

