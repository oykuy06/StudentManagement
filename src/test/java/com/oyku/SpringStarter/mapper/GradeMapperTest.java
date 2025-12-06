package com.oyku.SpringStarter.mapper;

import com.oyku.SpringStarter.DTO.ResponseDTO.GradeResponseDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.CourseSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.StudentSummaryDTO;
import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.model.Grade;
import com.oyku.SpringStarter.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradeMapperTest {

    private GradeMapper gradeMapper;

    @BeforeEach
    void setUp() {
        gradeMapper = new GradeMapper();
    }

    @Test
    void toDTO_shouldMapAllFields() {
        Student student = new Student(); student.setId(1); student.setName("Alice");
        Course course = new Course(); course.setId(10); course.setName("Java");

        Grade grade = new Grade();
        grade.setId(100);
        grade.setScore(95.0);
        grade.setStudent(student);
        grade.setCourse(course);

        GradeResponseDTO dto = gradeMapper.toDTO(grade);

        assertNotNull(dto);
        assertEquals(100, dto.getId());
        assertEquals(95.0, dto.getScore());
        assertNotNull(dto.getStudent());
        assertEquals("Alice", dto.getStudent().getName());
        assertNotNull(dto.getCourse());
        assertEquals("Java", dto.getCourse().getName());
    }

    @Test
    void toDTO_shouldReturnNull_whenGradeIsNull() {
        assertNull(gradeMapper.toDTO(null));
    }
}
