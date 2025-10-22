package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.model.Book;
import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.repository.BookRepository;
import com.oyku.SpringStarter.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBook(){
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(int id){
        return bookRepository.findById(id);
    }

    public Book addNewBook(Book book){
        return bookRepository.save(book);
    }

    public void deleteBook(int id){
        bookRepository.deleteById(id);
    }

    public void updateBook(Book book){
        bookRepository.save(book);
    }
}
