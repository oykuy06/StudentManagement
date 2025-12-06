package com.oyku.SpringStarter.mapper;

import com.oyku.SpringStarter.DTO.ResponseDTO.TeacherResponseDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.CourseSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.DepartmentSummaryDTO;
import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.model.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeacherMapperTest {

    private TeacherMapper teacherMapper;

    @BeforeEach
    void setUp() {
        teacherMapper = new TeacherMapper();
    }

    @Test
    void toDTO_shouldMapAllFields() {
        Department dept = new Department(); dept.setId(1); dept.setName("CS");
        Course c1 = new Course(); c1.setId(2); c1.setName("Java");
        Course c2 = new Course(); c2.setId(3); c2.setName("Python");

        Teacher teacher = new Teacher();
        teacher.setId(100);
        teacher.setName("Dr. Smith");
        teacher.setTitle("Professor");
        teacher.setDepartment(dept);
        teacher.setCourses(List.of(c1, c2));

        TeacherResponseDTO dto = teacherMapper.toDTO(teacher);

        assertNotNull(dto);
        assertEquals("Dr. Smith", dto.getName());
        assertEquals("CS", dto.getDepartment().getName());
        assertEquals(2, dto.getCourses().size());
        assertEquals("Python", dto.getCourses().get(1).getName());
    }

    @Test
    void toDTO_shouldReturnNull_whenTeacherIsNull() {
        assertNull(teacherMapper.toDTO(null));
    }
}
