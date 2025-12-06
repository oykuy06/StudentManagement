package com.oyku.SpringStarter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oyku.SpringStarter.DTO.RequestDTO.StudentRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.StudentResponseDTO;
import com.oyku.SpringStarter.exception.GlobalExceptionHandler;
import com.oyku.SpringStarter.mapper.StudentMapper;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.service.StudentService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentController studentController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllStudents_shouldReturnStudents() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();

        Student student = new Student();
        student.setId(1);
        student.setName("Alice");

        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(1);
        dto.setName("Alice");

        when(studentService.getAllStudents()).thenReturn(List.of(student));
        when(studentMapper.toDTO(student)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").value("Alice"));

        verify(studentService).getAllStudents();
    }

    @Test
    void getStudentById_shouldReturnStudent() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();

        Student student = new Student();
        student.setId(1);
        student.setName("Bob");

        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(1);
        dto.setName("Bob");

        when(studentService.getStudentById(1)).thenReturn(student);
        when(studentMapper.toDTO(student)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Bob"));

        verify(studentService).getStudentById(1);
    }

    @Test
    void getStudentById_shouldReturn404_whenNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        when(studentService.getStudentById(99)).thenThrow(new EntityNotFoundException("Student not found"));

        mockMvc.perform(get("/api/v1/students/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Student not found"));
    }

    @Test
    void createStudent_shouldReturnCreatedStudent() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();

        StudentRequestDTO requestDTO = new StudentRequestDTO();
        requestDTO.setName("Charlie");

        Student student = new Student();
        student.setId(1);
        student.setName("Charlie");

        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(1);
        dto.setName("Charlie");

        when(studentService.createStudent(any(StudentRequestDTO.class))).thenReturn(student);
        when(studentMapper.toDTO(student)).thenReturn(dto);

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Charlie"))
                .andExpect(header().string("Location", "/api/v1/students/1"));

        verify(studentService).createStudent(any(StudentRequestDTO.class));
    }

    @Test
    void createStudent_shouldReturnBadRequest_whenNameNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        StudentRequestDTO requestDTO = new StudentRequestDTO();
        requestDTO.setName(null); // invalid

        when(studentService.createStudent(any())).thenThrow(new IllegalArgumentException("Name cannot be null"));

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Name cannot be null"));
    }

    @Test
    void updateStudent_shouldReturnUpdatedStudent() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();

        StudentRequestDTO requestDTO = new StudentRequestDTO();
        requestDTO.setName("UpdatedName");

        Student student = new Student();
        student.setId(1);
        student.setName("UpdatedName");

        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(1);
        dto.setName("UpdatedName");

        when(studentService.updateStudent(eq(1), any(StudentRequestDTO.class))).thenReturn(student);
        when(studentMapper.toDTO(student)).thenReturn(dto);

        mockMvc.perform(put("/api/v1/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("UpdatedName"));

        verify(studentService).updateStudent(eq(1), any(StudentRequestDTO.class));
    }

    @Test
    void updateStudent_shouldReturnBadRequest_whenNameNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        StudentRequestDTO requestDTO = new StudentRequestDTO();
        requestDTO.setName(null); // invalid

        when(studentService.updateStudent(eq(1), any())).thenThrow(new IllegalArgumentException("Name cannot be null"));

        mockMvc.perform(put("/api/v1/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Name cannot be null"));
    }

    @Test
    void deleteStudent_shouldReturnNoContent() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();

        doNothing().when(studentService).deleteStudent(1);

        mockMvc.perform(delete("/api/v1/students/1"))
                .andExpect(status().isNoContent());

        verify(studentService).deleteStudent(1);
    }

    @Test
    void deleteStudent_shouldReturn404_whenNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        doThrow(new EntityNotFoundException("Student not found"))
                .when(studentService).deleteStudent(99);

        mockMvc.perform(delete("/api/v1/students/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Student not found"));

        verify(studentService).deleteStudent(99);
    }
}