package com.oyku.SpringStarter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oyku.SpringStarter.DTO.RequestDTO.TeacherRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.TeacherResponseDTO;
import com.oyku.SpringStarter.exception.GlobalExceptionHandler;
import com.oyku.SpringStarter.mapper.TeacherMapper;
import com.oyku.SpringStarter.model.Teacher;
import com.oyku.SpringStarter.service.TeacherService;
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
public class TeacherControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllTeachers_shouldReturnTeachers() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();

        Teacher teacher = new Teacher();
        teacher.setId(1);
        teacher.setName("Mr. Smith");

        TeacherResponseDTO dto = new TeacherResponseDTO();
        dto.setId(1);
        dto.setName("Mr. Smith");

        when(teacherService.getAllTeachers()).thenReturn(List.of(teacher));
        when(teacherMapper.toDTO(teacher)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/teachers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").value("Mr. Smith"));

        verify(teacherService).getAllTeachers();
    }

    @Test
    void getTeacherById_shouldReturnTeacher() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();

        Teacher teacher = new Teacher();
        teacher.setId(1);
        teacher.setName("Mrs. Johnson");

        TeacherResponseDTO dto = new TeacherResponseDTO();
        dto.setId(1);
        dto.setName("Mrs. Johnson");

        when(teacherService.getTeacherById(1)).thenReturn(teacher);
        when(teacherMapper.toDTO(teacher)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/teachers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Mrs. Johnson"));

        verify(teacherService).getTeacherById(1);
    }

    @Test
    void getTeacherById_shouldReturn404_whenNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        when(teacherService.getTeacherById(99)).thenThrow(new EntityNotFoundException("Teacher not found"));

        mockMvc.perform(get("/api/v1/teachers/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Teacher not found"));
    }

    @Test
    void addTeacher_shouldReturnCreatedTeacher() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();

        TeacherRequestDTO requestDTO = new TeacherRequestDTO();
        requestDTO.setName("Dr. Adams");
        requestDTO.setTitle("Professor");
        requestDTO.setDepartmentId(1);
        requestDTO.setCourseIds(List.of(2, 3));

        Teacher teacher = new Teacher();
        teacher.setId(1);
        teacher.setName("Dr. Adams");

        TeacherResponseDTO dto = new TeacherResponseDTO();
        dto.setId(1);
        dto.setName("Dr. Adams");

        when(teacherService.addNewTeacher(any(TeacherRequestDTO.class))).thenReturn(teacher);
        when(teacherMapper.toDTO(teacher)).thenReturn(dto);

        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Dr. Adams"))
                .andExpect(header().string("Location", "/api/v1/teachers/1"));

        verify(teacherService).addNewTeacher(any(TeacherRequestDTO.class));
    }

    @Test
    void addTeacher_shouldReturnBadRequest_whenNameNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        TeacherRequestDTO requestDTO = new TeacherRequestDTO();
        requestDTO.setName(null); // invalid
        requestDTO.setTitle("Professor");
        requestDTO.setDepartmentId(1);
        requestDTO.setCourseIds(List.of(2, 3));

        when(teacherService.addNewTeacher(any())).thenThrow(new IllegalArgumentException("Name cannot be null"));

        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Name cannot be null"));
    }

    @Test
    void updateTeacher_shouldReturnUpdatedTeacher() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();

        TeacherRequestDTO requestDTO = new TeacherRequestDTO();
        requestDTO.setName("Dr. Updated");
        requestDTO.setTitle("Lecturer");
        requestDTO.setDepartmentId(2);
        requestDTO.setCourseIds(List.of(4, 5));

        Teacher teacher = new Teacher();
        teacher.setId(1);
        teacher.setName("Dr. Updated");

        TeacherResponseDTO dto = new TeacherResponseDTO();
        dto.setId(1);
        dto.setName("Dr. Updated");

        when(teacherService.updateTeacher(eq(1), any(TeacherRequestDTO.class))).thenReturn(teacher);
        when(teacherMapper.toDTO(teacher)).thenReturn(dto);

        mockMvc.perform(put("/api/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Dr. Updated"));

        verify(teacherService).updateTeacher(eq(1), any(TeacherRequestDTO.class));
    }

    @Test
    void updateTeacher_shouldReturnBadRequest_whenTitleNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        TeacherRequestDTO requestDTO = new TeacherRequestDTO();
        requestDTO.setName("Dr. Adams");
        requestDTO.setTitle(null); // invalid
        requestDTO.setDepartmentId(1);
        requestDTO.setCourseIds(List.of(2, 3));

        when(teacherService.updateTeacher(eq(1), any())).thenThrow(new IllegalArgumentException("Title cannot be null"));

        mockMvc.perform(put("/api/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Title cannot be null"));
    }

    @Test
    void deleteTeacher_shouldReturnNoContent() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();

        doNothing().when(teacherService).deleteTeacherById(1);

        mockMvc.perform(delete("/api/v1/teachers/1"))
                .andExpect(status().isNoContent());

        verify(teacherService).deleteTeacherById(1);
    }

    @Test
    void deleteTeacher_shouldReturn404_whenNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        doThrow(new EntityNotFoundException("Teacher not found"))
                .when(teacherService).deleteTeacherById(99);

        mockMvc.perform(delete("/api/v1/teachers/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Teacher not found"));

        verify(teacherService).deleteTeacherById(99);
    }
}