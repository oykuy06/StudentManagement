package com.oyku.SpringStarter.repository;

import com.oyku.SpringStarter.model.Book;
import com.oyku.SpringStarter.model.Library;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LibraryRepositoryTest {

    @Autowired
    private LibraryRepository libraryRepository;

    @Test
    void testSaveAndFindLibrary() {
        Library library = new Library("Central Library", "Main Street");
        libraryRepository.save(library);

        Library found = libraryRepository.findById(library.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Central Library");
        assertThat(found.getLocation()).isEqualTo("Main Street");
    }

    @Test
    void testLibraryWithBooks() {
        Library library = new Library("City Library", "Downtown");

        Book book1 = new Book("Book One");
        Book book2 = new Book("Book Two");

        book1.setLibrary(library);
        book2.setLibrary(library);

        library.getBooks().add(book1);
        library.getBooks().add(book2);

        libraryRepository.save(library);

        Library foundLibrary = libraryRepository.findById(library.getId()).orElse(null);

        assertThat(foundLibrary).isNotNull();
        assertThat(foundLibrary.getBooks()).hasSize(2);
    }
}
