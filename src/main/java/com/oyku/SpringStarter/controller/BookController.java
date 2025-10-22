package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.model.Book;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.service.BookService;
import com.oyku.SpringStarter.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final StudentService studentService;

    @Autowired
    public BookController(BookService bookService, StudentService studentService) {
        this.bookService = bookService;
        this.studentService = studentService;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBook();
    }

    @GetMapping("/{id}")
    public Optional<Book> getBookById(@PathVariable int id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public ResponseEntity<Book> addNewBook(@RequestBody Book book) {
        // Student ile ilişkilendir
        if (book.getStudent() != null && book.getStudent().getId() != 0) {
            studentService.getStudentById(book.getStudent().getId()).ifPresent(book::setStudent);
        }
        Book saved = bookService.addNewBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable int id, @RequestBody Book book) {
        Optional<Book> optionalBook = bookService.getBookById(id);
        if (optionalBook.isEmpty()) return ResponseEntity.notFound().build();

        Book b = optionalBook.get();
        b.setTitle(book.getTitle());
        if (book.getStudent() != null && book.getStudent().getId() != 0) {
            studentService.getStudentById(book.getStudent().getId()).ifPresent(b::setStudent);
        }

        bookService.updateBook(b);
        return ResponseEntity.ok(b);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book deleted with id: " + id);
    }
}
