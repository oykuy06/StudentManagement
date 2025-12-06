package com.oyku.SpringStarter.mapper;

import com.oyku.SpringStarter.DTO.ResponseDTO.BookResponseDTO;
import com.oyku.SpringStarter.model.Book;
import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.model.Library;
import com.oyku.SpringStarter.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookMapperTest {

    private BookMapper bookMapper;

    @BeforeEach
    void setUp() {
        bookMapper = new BookMapper();
    }

    @Test
    void toDTO_shouldMapAllFields() {
        Book book = new Book();
        book.setId(1);
        book.setTitle("Test Book");

        Department department = new Department();
        department.setId(10);
        department.setName("IT");

        Student student = new Student();
        student.setId(100);
        student.setName("Alice");
        student.setDepartment(department);

        book.setStudent(student);

        Library library = new Library();
        library.setId(50);
        library.setName("Central Library");
        library.setLocation("Main Street");

        book.setLibrary(library);

        // Mapper
        BookResponseDTO dto = bookMapper.toDTO(book);

        // Assertions
        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("Test Book", dto.getTitle());

        // Student assertions
        assertNotNull(dto.getStudent());
        assertEquals(100, dto.getStudent().getId());
        assertEquals("Alice", dto.getStudent().getName());
        assertNotNull(dto.getStudent().getDepartment());
        assertEquals(10, dto.getStudent().getDepartment().getId());
        assertEquals("IT", dto.getStudent().getDepartment().getName());

        // Library assertions
        assertNotNull(dto.getLibrary());
        assertEquals(50, dto.getLibrary().getId());
        assertEquals("Central Library", dto.getLibrary().getName());
        assertEquals("Main Street", dto.getLibrary().getLocation());
    }

    @Test
    void toDTO_shouldReturnNull_whenBookIsNull() {
        assertNull(bookMapper.toDTO(null));
    }
}
