package com.oyku.SpringStarter.mapper;

import com.oyku.SpringStarter.DTO.ResponseDTO.DepartmentResponseDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.StudentSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.TeacherSummaryDTO;
import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.model.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentMapperTest {

    private DepartmentMapper departmentMapper;

    @BeforeEach
    void setUp() {
        departmentMapper = new DepartmentMapper();
    }

    @Test
    void toDTO_shouldMapAllFields() {
        // Students
        Student s1 = new Student(); s1.setId(1); s1.setName("Alice");
        Student s2 = new Student(); s2.setId(2); s2.setName("Bob");

        // Teachers
        Teacher t1 = new Teacher(); t1.setId(10); t1.setName("Dr. Smith"); t1.setTitle("Professor");
        Teacher t2 = new Teacher(); t2.setId(11); t2.setName("Dr. John"); t2.setTitle("Lecturer");

        Department dep = new Department();
        dep.setId(100);
        dep.setName("IT");
        dep.setStudents(List.of(s1, s2));
        dep.setTeachers(List.of(t1, t2));

        DepartmentResponseDTO dto = departmentMapper.toDTO(dep);

        assertNotNull(dto);
        assertEquals(100, dto.getId());
        assertEquals("IT", dto.getName());

        assertEquals(2, dto.getStudents().size());
        assertEquals("Alice", dto.getStudents().get(0).getName());

        assertEquals(2, dto.getTeachers().size());
        assertEquals("Dr. John", dto.getTeachers().get(1).getName());
    }

    @Test
    void toDTO_shouldReturnNull_whenDepartmentIsNull() {
        assertNull(departmentMapper.toDTO(null));
    }
}
