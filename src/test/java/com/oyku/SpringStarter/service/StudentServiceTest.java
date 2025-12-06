package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.DTO.RequestDTO.StudentRequestDTO;
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
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private StudentProfileRepository profileRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private Department department;
    private StudentProfile profile;

    @BeforeEach
    void setup() {
        student = new Student();
        student.setId(1);
        student.setName("Test Student");

        department = new Department();
        department.setId(1);

        profile = new StudentProfile();
        profile.setId(1);
    }

    @Test
    void getAllStudents_shouldReturnList() {
        when(studentRepository.findAll()).thenReturn(List.of(student));

        List<Student> result = studentService.getAllStudents();

        assertEquals(1, result.size());
        verify(studentRepository).findAll();
    }

    @Test
    void getStudentById_shouldReturnStudent_whenExists() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1);

        assertEquals("Test Student", result.getName());
        verify(studentRepository).findById(1);
    }

    @Test
    void getStudentById_shouldThrowException_whenNotFound() {
        when(studentRepository.findById(2)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> studentService.getStudentById(2));

        assertTrue(ex.getMessage().contains("Student not found"));
    }

    @Test
    void addNewStudent_shouldSaveAndReturn() {
        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setName("New Student");
        dto.setDepartmentId(1);
        dto.setProfileId(1);

        when(departmentRepository.findById(1)).thenReturn(Optional.of(department));
        when(profileRepository.findById(1)).thenReturn(Optional.of(profile));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student result = studentService.createStudent(dto);

        assertNotNull(result);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void updateStudent_shouldUpdate_whenExists() {
        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setName("Updated Student");
        dto.setDepartmentId(1);
        dto.setProfileId(1);

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(departmentRepository.findById(1)).thenReturn(Optional.of(department));
        when(profileRepository.findById(1)).thenReturn(Optional.of(profile));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student result = studentService.updateStudent(1, dto);

        assertEquals("Updated Student", result.getName());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void deleteStudent_shouldDelete_whenExists() {
        when(studentRepository.existsById(1)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(1);

        assertDoesNotThrow(() -> studentService.deleteStudent(1));
        verify(studentRepository).deleteById(1);
    }

    @Test
    void deleteStudent_shouldThrow_whenNotFound() {
        when(studentRepository.existsById(2)).thenReturn(false);

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> studentService.deleteStudent(2));

        assertTrue(ex.getMessage().contains("Student not found"));
    }
}
