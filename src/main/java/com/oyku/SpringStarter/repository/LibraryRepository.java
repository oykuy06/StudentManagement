package com.oyku.SpringStarter.repository;

import com.oyku.SpringStarter.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Integer> {
}
