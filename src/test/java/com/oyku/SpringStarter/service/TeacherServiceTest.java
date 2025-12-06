package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.DTO.RequestDTO.TeacherRequestDTO;
import com.oyku.SpringStarter.model.*;
import com.oyku.SpringStarter.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;
    private Department department;
    private Course course;

    @BeforeEach
    void setup() {
        teacher = new Teacher();
        teacher.setId(1);
        teacher.setName("Test Teacher");

        department = new Department();
        department.setId(1);

        course = new Course();
        course.setId(1);
    }

    @Test
    void getAllTeachers_shouldReturnList() {
        when(teacherRepository.findAll()).thenReturn(List.of(teacher));

        List<Teacher> result = teacherService.getAllTeachers();

        assertEquals(1, result.size());
        verify(teacherRepository).findAll();
    }

    @Test
    void getTeacherById_shouldReturnTeacher_whenExists() {
        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));

        Teacher result = teacherService.getTeacherById(1);

        assertEquals("Test Teacher", result.getName());
        verify(teacherRepository).findById(1);
    }

    @Test
    void getTeacherById_shouldThrowException_whenNotFound() {
        when(teacherRepository.findById(2)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> teacherService.getTeacherById(2));

        assertTrue(ex.getMessage().contains("Teacher not found"));
    }

    @Test
    void addNewTeacher_shouldSaveAndReturn() {
        TeacherRequestDTO dto = new TeacherRequestDTO();
        dto.setName("New Teacher");
        dto.setTitle("Professor");
        dto.setDepartmentId(1);
        dto.setCourseIds(List.of(1));

        when(departmentRepository.findById(1)).thenReturn(Optional.of(department));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);

        Teacher result = teacherService.addNewTeacher(dto);

        assertNotNull(result);
        verify(teacherRepository).save(any(Teacher.class));
    }
}
