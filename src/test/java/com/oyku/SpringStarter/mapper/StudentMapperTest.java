package com.oyku.SpringStarter.mapper;

import com.oyku.SpringStarter.DTO.ResponseDTO.StudentResponseDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.BookSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.CourseSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.DepartmentSummaryDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.StudentProfileResponseDTO;
import com.oyku.SpringStarter.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentMapperTest {

    private StudentMapper studentMapper;

    @BeforeEach
    void setUp() {
        studentMapper = new StudentMapper();
    }

    @Test
    void toDTO_shouldMapAllFields() {
        Department dept = new Department(); dept.setId(1); dept.setName("IT");
        StudentProfile profile = new StudentProfile(); profile.setId(2); profile.setAddress("Street"); profile.setPhone("12345");
        Book book = new Book(); book.setId(3); book.setTitle("Java Book");
        Course course = new Course(); course.setId(4); course.setName("Java");

        Student student = new Student();
        student.setId(100);
        student.setName("Alice");
        student.setDepartment(dept);
        student.setProfile(profile);
        student.setBooks(List.of(book));
        student.setCourses(List.of(course));

        StudentResponseDTO dto = studentMapper.toDTO(student);

        assertNotNull(dto);
        assertEquals(100, dto.getId());
        assertEquals("Alice", dto.getName());
        assertEquals("IT", dto.getDepartment().getName());
        assertEquals("Street", dto.getProfile().getAddress());
        assertEquals(1, dto.getBooks().size());
        assertEquals("Java Book", dto.getBooks().get(0).getTitle());
        assertEquals(1, dto.getCourses().size());
        assertEquals("Java", dto.getCourses().get(0).getName());
    }

    @Test
    void toDTO_shouldReturnNull_whenStudentIsNull() {
        assertNull(studentMapper.toDTO(null));
    }
}
