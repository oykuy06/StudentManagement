package com.oyku.SpringStarter.repository;

import com.oyku.SpringStarter.model.Book;
import com.oyku.SpringStarter.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
