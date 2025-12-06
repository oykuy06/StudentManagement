package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.DTO.RequestDTO.CourseRequestDTO;
import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.repository.CourseRepository;
import com.oyku.SpringStarter.repository.DepartmentRepository;
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
public class CourseServiceTest {

    @Mock private CourseRepository courseRepository;
    @Mock private DepartmentRepository departmentRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course;
    private Department department;

    @BeforeEach
    void setup() {
        department = new Department();
        department.setId(1);

        course = new Course();
        course.setId(1);
        course.setName("Test Course");
    }

    // GET
    @Test
    void getAllCourses_shouldReturnList() {
        when(courseRepository.findAll()).thenReturn(List.of(course));
        var result = courseService.getAllCourses();
        assertEquals(1, result.size());
        verify(courseRepository).findAll();
    }

    @Test
    void getCourseById_shouldReturn_whenExists() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        var result = courseService.getCourseById(1);
        assertEquals("Test Course", result.getName());
    }

    @Test
    void getCourseById_shouldThrow_whenNotFound() {
        when(courseRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> courseService.getCourseById(99));
    }

    // CREATE
    @Test
    void createCourse_shouldSave_whenValid() {
        CourseRequestDTO dto = new CourseRequestDTO();
        dto.setName("New Course");

        when(departmentRepository.findById(1)).thenReturn(Optional.of(department));
        when(courseRepository.save(any())).thenReturn(course);

        Course result = courseService.createCourse(dto);
        assertNotNull(result);
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void createCourse_shouldThrow_whenDepartmentMissing() {
        CourseRequestDTO dto = new CourseRequestDTO();
        dto.setName("New Course");

        when(departmentRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> courseService.createCourse(dto));
    }

    @Test
    void createCourse_shouldThrow_whenNameNull() {
        CourseRequestDTO dto = new CourseRequestDTO();
        dto.setName(null);

        assertThrows(IllegalArgumentException.class,
                () -> courseService.createCourse(dto));
    }

    // UPDATE
    @Test
    void updateCourse_shouldUpdate_whenValid() {
        CourseRequestDTO dto = new CourseRequestDTO();
        dto.setName("Updated Course");

        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(departmentRepository.findById(1)).thenReturn(Optional.of(department));
        when(courseRepository.save(any())).thenReturn(course);

        var updated = courseService.updateCourse(1, dto);
        assertEquals("Updated Course", updated.getName());
    }

    @Test
    void updateCourse_shouldThrow_whenCourseMissing() {
        CourseRequestDTO dto = new CourseRequestDTO();
        dto.setName("Updated");

        when(courseRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> courseService.updateCourse(99, dto));
    }

    @Test
    void updateCourse_shouldThrow_whenDepartmentMissing() {
        CourseRequestDTO dto = new CourseRequestDTO();
        dto.setName("Updated");

        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(departmentRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> courseService.updateCourse(1, dto));
    }

    @Test
    void updateCourse_shouldThrow_whenNameNull() {
        CourseRequestDTO dto = new CourseRequestDTO();
        dto.setName(null);

        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        assertThrows(IllegalArgumentException.class,
                () -> courseService.updateCourse(1, dto));
    }

    // DELETE
    @Test
    void deleteCourse_shouldDelete_whenExists() {
        when(courseRepository.existsById(1)).thenReturn(true);

        assertDoesNotThrow(() -> courseService.deleteCourse(1));
        verify(courseRepository).deleteById(1);
    }

    @Test
    void deleteCourse_shouldThrow_whenNotExists() {
        when(courseRepository.existsById(999)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> courseService.deleteCourse(999));
    }
}
