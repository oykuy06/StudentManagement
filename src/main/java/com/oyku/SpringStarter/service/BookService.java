package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.DTO.RequestDTO.BookRequestDTO;
import com.oyku.SpringStarter.model.Book;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.model.Library;
import com.oyku.SpringStarter.repository.BookRepository;
import com.oyku.SpringStarter.repository.StudentRepository;
import com.oyku.SpringStarter.repository.LibraryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private final LibraryRepository libraryRepository;

    public BookService(BookRepository bookRepository,
                       StudentRepository studentRepository,
                       LibraryRepository libraryRepository) {
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
        this.libraryRepository = libraryRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(int id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + id));
    }

    public Book createBook(BookRequestDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + dto.getStudentId()));
        Library library = libraryRepository.findById(dto.getLibraryId())
                .orElseThrow(() -> new EntityNotFoundException("Library not found with ID: " + dto.getLibraryId()));

        book.setStudent(student);
        book.setLibrary(library);

        return bookRepository.save(book);
    }

    public Book updateBook(int id, BookRequestDTO dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + id));

        book.setTitle(dto.getTitle());

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + dto.getStudentId()));
        Library library = libraryRepository.findById(dto.getLibraryId())
                .orElseThrow(() -> new EntityNotFoundException("Library not found with ID: " + dto.getLibraryId()));

        book.setStudent(student);
        book.setLibrary(library);

        return bookRepository.save(book);
    }

    public void deleteBook(int id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found with ID: " + id);
        }
        bookRepository.deleteById(id);
    }

}
