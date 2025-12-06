package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.DTO.RequestDTO.BookRequestDTO;
import com.oyku.SpringStarter.model.Book;
import com.oyku.SpringStarter.model.Library;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.repository.BookRepository;
import com.oyku.SpringStarter.repository.LibraryRepository;
import com.oyku.SpringStarter.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private LibraryRepository libraryRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private Student student;
    private Library library;

    @BeforeEach
    void setup() {
        student = new Student();
        student.setId(1);

        library = new Library();
        library.setId(1);

        book = new Book();
        book.setId(1);
        book.setTitle("Test Book");
        book.setStudent(student);
        book.setLibrary(library);
    }

    // ---------------------------------------------------------
    // GET
    // ---------------------------------------------------------

    @Test
    void getAllBooks_shouldReturnBookList() {
        when(bookRepository.findAll()).thenReturn(List.of(book));

        var result = bookService.getAllBooks();

        assertEquals(1, result.size());
        verify(bookRepository).findAll();
    }

    @Test
    void getBookById_shouldReturnBook_whenExists() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        Book result = bookService.getBookById(1);

        assertEquals("Test Book", result.getTitle());
        verify(bookRepository).findById(1);
    }

    @Test
    void getBookById_shouldThrow_whenNotExists() {
        when(bookRepository.findById(999)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.getBookById(999)
        );

        assertTrue(ex.getMessage().contains("Book not found"));
    }

    // ---------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------

    @Test
    void createBook_shouldSave_whenValid() {
        BookRequestDTO dto = new BookRequestDTO();
        dto.setTitle("New Book");
        dto.setStudentId(1);
        dto.setLibraryId(1);

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(libraryRepository.findById(1)).thenReturn(Optional.of(library));
        when(bookRepository.save(any())).thenReturn(book);

        Book result = bookService.createBook(dto);

        assertNotNull(result);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void createBook_shouldThrow_whenStudentNotFound() {
        BookRequestDTO dto = new BookRequestDTO();
        dto.setTitle("New Book");
        dto.setStudentId(99);
        dto.setLibraryId(1);

        when(studentRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> bookService.createBook(dto));
    }

    @Test
    void createBook_shouldThrow_whenLibraryNotFound() {
        BookRequestDTO dto = new BookRequestDTO();
        dto.setTitle("New Book");
        dto.setStudentId(1);
        dto.setLibraryId(999);

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(libraryRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> bookService.createBook(dto));
    }

    @Test
    void createBook_shouldThrow_whenTitleIsNull() {
        BookRequestDTO dto = new BookRequestDTO();
        dto.setTitle(null);
        dto.setStudentId(1);
        dto.setLibraryId(1);

        assertThrows(IllegalArgumentException.class,
                () -> bookService.createBook(dto));
    }

    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------

    @Test
    void updateBook_shouldUpdate_whenValid() {
        BookRequestDTO dto = new BookRequestDTO();
        dto.setTitle("Updated Book");
        dto.setStudentId(1);
        dto.setLibraryId(1);

        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(libraryRepository.findById(1)).thenReturn(Optional.of(library));
        when(bookRepository.save(any())).thenReturn(book);

        Book result = bookService.updateBook(1, dto);

        assertEquals("Updated Book", result.getTitle());
    }

    @Test
    void updateBook_shouldThrow_whenBookNotFound() {
        BookRequestDTO dto = new BookRequestDTO();
        dto.setTitle("Updated Book");

        when(bookRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> bookService.updateBook(99, dto));
    }

    @Test
    void updateBook_shouldThrow_whenStudentNotFound() {
        BookRequestDTO dto = new BookRequestDTO();
        dto.setTitle("Updated Book");
        dto.setStudentId(999);
        dto.setLibraryId(1);

        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(studentRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> bookService.updateBook(1, dto));
    }

    @Test
    void updateBook_shouldThrow_whenLibraryNotFound() {
        BookRequestDTO dto = new BookRequestDTO();
        dto.setTitle("Updated Book");
        dto.setStudentId(1);
        dto.setLibraryId(999);

        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(libraryRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> bookService.updateBook(1, dto));
    }

    @Test
    void updateBook_shouldThrow_whenTitleNull() {
        BookRequestDTO dto = new BookRequestDTO();
        dto.setTitle(null);
        dto.setStudentId(1);
        dto.setLibraryId(1);

        when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        assertThrows(IllegalArgumentException.class,
                () -> bookService.updateBook(1, dto));
    }

    // ---------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------

    @Test
    void deleteBook_shouldDelete_whenExists() {
        when(bookRepository.existsById(1)).thenReturn(true);

        assertDoesNotThrow(() -> bookService.deleteBook(1));
        verify(bookRepository).deleteById(1);
    }

    @Test
    void deleteBook_shouldThrow_whenNotExists() {
        when(bookRepository.existsById(999)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> bookService.deleteBook(999));
    }
}
