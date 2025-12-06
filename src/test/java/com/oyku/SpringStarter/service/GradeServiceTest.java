package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.model.Grade;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.repository.CourseRepository;
import com.oyku.SpringStarter.repository.GradeRepository;
import com.oyku.SpringStarter.repository.StudentRepository;
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
public class GradeServiceTest {

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private GradeService gradeService;

    private Student student;
    private Course course;
    private Grade grade;

    @BeforeEach
    void setup() {
        student = new Student();
        student.setId(1);

        course = new Course();
        course.setId(1);

        grade = new Grade();
        grade.setId(1);
        grade.setScore(85.5);
        grade.setCourse(course);
        grade.setStudent(student);
    }

    // -------------------- GET --------------------

    @Test
    void getAllGrades_shouldReturnList() {
        when(gradeRepository.findAll()).thenReturn(List.of(grade));

        List<Grade> result = gradeService.getAllGrades();

        assertEquals(1, result.size());
        verify(gradeRepository, times(1)).findAll();
    }

    @Test
    void getGradeById_shouldReturnGrade_whenExists() {
        when(gradeRepository.findById(1)).thenReturn(Optional.of(grade));

        Grade result = gradeService.getGradeById(1);

        assertEquals(85, result.getScore());
        verify(gradeRepository).findById(1);
    }

    @Test
    void getGradeById_shouldThrowException_whenNotFound() {
        when(gradeRepository.findById(2)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> gradeService.getGradeById(2));

        assertTrue(ex.getMessage().contains("Grade not found"));
    }

    // -------------------- CREATE --------------------

    @Test
    void createGrade_shouldSaveAndReturn() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(gradeRepository.save(any(Grade.class))).thenReturn(grade);

        Grade result = gradeService.createGrade(90.5, 1, 1);

        assertNotNull(result);
        assertEquals(85, result.getScore()); // mock Grade döndüğü için 85
        verify(gradeRepository).save(any(Grade.class));
    }

    @Test
    void createGrade_shouldThrow_whenStudentNotFound() {
        when(studentRepository.findById(99)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> gradeService.createGrade(90.5, 99, 1));

        assertTrue(ex.getMessage().contains("Student not found"));
    }

    @Test
    void createGrade_shouldThrow_whenCourseNotFound() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(99)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> gradeService.createGrade(90.5, 1, 99));

        assertTrue(ex.getMessage().contains("Course not found"));
    }

    // -------------------- UPDATE --------------------

    @Test
    void updateGrade_shouldUpdate_whenExists() {
        when(gradeRepository.findById(1)).thenReturn(Optional.of(grade));
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(gradeRepository.save(any(Grade.class))).thenReturn(grade);

        Grade result = gradeService.updateGrade(1, 95.5, 1, 1);

        assertEquals(95, result.getScore());
        verify(gradeRepository).save(any(Grade.class));
    }

    @Test
    void updateGrade_shouldThrow_whenGradeNotFound() {
        when(gradeRepository.findById(99)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> gradeService.updateGrade(99, 95.5, 1, 1));

        assertTrue(ex.getMessage().contains("Grade not found"));
    }

    // -------------------- DELETE --------------------

    @Test
    void deleteGrade_shouldDelete_whenExists() {
        when(gradeRepository.existsById(1)).thenReturn(true);
        doNothing().when(gradeRepository).deleteById(1);

        assertDoesNotThrow(() -> gradeService.deleteGrade(1));
        verify(gradeRepository).deleteById(1);
    }

    @Test
    void deleteGrade_shouldThrowException_whenNotFound() {
        when(gradeRepository.existsById(2)).thenReturn(false);

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> gradeService.deleteGrade(2));

        assertTrue(ex.getMessage().contains("Grade not found"));
    }
}
