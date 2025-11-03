package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.DTO.RequestDTO.BookRequestDTO;
import com.oyku.SpringStarter.model.Book;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.model.Library;
import com.oyku.SpringStarter.repository.BookRepository;
import com.oyku.SpringStarter.repository.StudentRepository;
import com.oyku.SpringStarter.repository.LibraryRepository;
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

    public Optional<Book> getBookById(int id) {
        return bookRepository.findById(id);
    }

    public Book createBook(BookRequestDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());

        if (dto.getStudentId() != 0) {
            studentRepository.findById(dto.getStudentId())
                    .ifPresent(book::setStudent);
        }

        if (dto.getLibraryId() != 0) {
            libraryRepository.findById(dto.getLibraryId())
                    .ifPresent(book::setLibrary);
        }

        return bookRepository.save(book);
    }

    public Optional<Book> updateBook(int id, BookRequestDTO dto) {
        Optional<Book> optional = bookRepository.findById(id);
        if (optional.isEmpty()) return Optional.empty();

        Book book = optional.get();
        book.setTitle(dto.getTitle());

        if (dto.getStudentId() != 0) {
            studentRepository.findById(dto.getStudentId())
                    .ifPresent(book::setStudent);
        } else {
            book.setStudent(null);
        }

        if (dto.getLibraryId() != 0) {
            libraryRepository.findById(dto.getLibraryId())
                    .ifPresent(book::setLibrary);
        } else {
            book.setLibrary(null);
        }

        bookRepository.save(book);
        return Optional.of(book);
    }

    public boolean deleteBook(int id) {
        if (!bookRepository.existsById(id)) return false;
        bookRepository.deleteById(id);
        return true;
    }
}
