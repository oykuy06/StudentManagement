package com.oyku.SpringStarter.mapper;

import com.oyku.SpringStarter.DTO.ResponseDTO.LibraryResponseDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.BookSummaryDTO;
import com.oyku.SpringStarter.model.Book;
import com.oyku.SpringStarter.model.Library;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibraryMapperTest {

    private LibraryMapper libraryMapper;

    @BeforeEach
    void setUp() {
        libraryMapper = new LibraryMapper();
    }

    @Test
    void toDTO_shouldMapAllFields() {
        Book b1 = new Book(); b1.setId(1); b1.setTitle("Book One");
        Book b2 = new Book(); b2.setId(2); b2.setTitle("Book Two");

        Library library = new Library();
        library.setId(100);
        library.setName("Central Library");
        library.setLocation("Main Street");
        library.setBooks(List.of(b1, b2));

        LibraryResponseDTO dto = libraryMapper.toDTO(library);

        assertNotNull(dto);
        assertEquals(100, dto.getId());
        assertEquals("Central Library", dto.getName());
        assertEquals("Main Street", dto.getLocation());
        assertEquals(2, dto.getBooks().size());
        assertEquals("Book One", dto.getBooks().get(0).getTitle());
    }

    @Test
    void toDTO_shouldReturnNull_whenLibraryIsNull() {
        assertNull(libraryMapper.toDTO(null));
    }
}
