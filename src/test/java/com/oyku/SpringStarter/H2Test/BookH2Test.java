package com.oyku.SpringStarter.H2Test;

import com.oyku.SpringStarter.model.*;
import com.oyku.SpringStarter.repository.BookRepository;
import com.oyku.SpringStarter.repository.DepartmentRepository;
import com.oyku.SpringStarter.repository.LibraryRepository;
import com.oyku.SpringStarter.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BookH2Test {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Student student;
    private Library library;
    private Book book;

    @BeforeEach
    void setup() {
        // Department oluştur
        Department dep = new Department();
        dep.setName("Engineering");
        departmentRepository.save(dep);

        // Profile oluştur (zorunlu alan yoksa boş instance yeterli)
        StudentProfile profile = new StudentProfile();

        // Student oluştur
        student = new Student("Alice");
        student.setDepartment(dep);
        student.setProfile(profile);
        student = studentRepository.save(student);

        // Library oluştur
        library = new Library();
        library.setName("Central Library");
        library.setLocation("Main Street");
        library = libraryRepository.save(library);

        // Book oluştur
        book = new Book();
        book.setTitle("H2 Test Book");
        book.setStudent(student);
        book.setLibrary(library);
        book = bookRepository.save(book);
    }

    @Test
    void testFindById() {
        Optional<Book> found = bookRepository.findById(book.getId());
        assertTrue(found.isPresent());
        assertEquals("H2 Test Book", found.get().getTitle());
        assertEquals(student.getId(), found.get().getStudent().getId());
        assertEquals(library.getId(), found.get().getLibrary().getId());
    }

    @Test
    void testSaveBook() {
        Department dep = new Department();
        dep.setName("Computer Science");
        departmentRepository.save(dep);

        StudentProfile profile = new StudentProfile();

        Student student = new Student("Test Student");
        student.setDepartment(dep);
        student.setProfile(profile);
        studentRepository.save(student);

        Library library = new Library();
        library.setName("Central Library");
        libraryRepository.save(library);

        Book newBook = new Book();
        newBook.setTitle("Another Book");
        newBook.setStudent(student);
        newBook.setLibrary(library);

        Book saved = bookRepository.save(newBook);

        assertNotNull(saved.getId());
        assertEquals("Another Book", saved.getTitle());
        assertEquals("Test Student", saved.getStudent().getName());
        assertEquals("Computer Science", saved.getStudent().getDepartment().getName());
    }

    @Test
    void testDeleteBook() {
        bookRepository.delete(book);
        Optional<Book> found = bookRepository.findById(book.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void testDeleteStudent_unlinksBooksButKeepsThem() {
        // book student relation
        for (Book b : bookRepository.findAll()) {
            if (b.getStudent() != null && b.getStudent().getId() == student.getId()) {
                b.setStudent(null);
                bookRepository.save(b);
            }
        }

        // delete student
        studentRepository.delete(student);

        List<Book> books = bookRepository.findAll();
        assertFalse(books.isEmpty(), "Books should remain even if student is deleted");
        assertNull(books.get(0).getStudent(), "Book should no longer be linked to deleted student");
    }

    @Test
    void testOrphanRemovalLibrary() {
        book.setLibrary(null);
        bookRepository.save(book);

        Book updated = bookRepository.findById(book.getId()).orElseThrow();
        assertNull(updated.getLibrary());
    }
}