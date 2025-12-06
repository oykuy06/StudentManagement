package com.oyku.SpringStarter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oyku.SpringStarter.DTO.RequestDTO.GradeRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.GradeResponseDTO;
import com.oyku.SpringStarter.exception.GlobalExceptionHandler;
import com.oyku.SpringStarter.mapper.GradeMapper;
import com.oyku.SpringStarter.model.Grade;
import com.oyku.SpringStarter.service.GradeService;
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
public class GradeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GradeService gradeService;

    @Mock
    private GradeMapper gradeMapper;

    @InjectMocks
    private GradeController gradeController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllGrades_shouldReturnGrades() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(gradeController).build();

        Grade grade = new Grade();
        grade.setId(1);
        grade.setScore(85.5);

        GradeResponseDTO dto = new GradeResponseDTO();
        dto.setId(1);
        dto.setScore(85.5);

        when(gradeService.getAllGrades()).thenReturn(List.of(grade));
        when(gradeMapper.toDTO(grade)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/grades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].score").value(85));

        verify(gradeService).getAllGrades();
    }

    @Test
    void getGradeById_shouldReturnGrade() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(gradeController).build();

        Grade grade = new Grade();
        grade.setId(1);
        grade.setScore(85.5);

        GradeResponseDTO dto = new GradeResponseDTO();
        dto.setId(1);
        dto.setScore(85.5);

        when(gradeService.getGradeById(1)).thenReturn(grade);
        when(gradeMapper.toDTO(grade)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/grades/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.score").value(85));

        verify(gradeService).getGradeById(1);
    }

    @Test
    void getGradeById_shouldReturn404_whenNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(gradeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        when(gradeService.getGradeById(99)).thenThrow(new EntityNotFoundException("Grade not found"));

        mockMvc.perform(get("/api/v1/grades/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Grade not found"));
    }

    @Test
    void createGrade_shouldReturnCreatedGrade() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(gradeController).build();

        GradeRequestDTO dto = new GradeRequestDTO();
        dto.setScore(85.5);
        dto.setStudentId(1);
        dto.setCourseId(1);

        Grade grade = new Grade();
        grade.setId(1);
        grade.setScore(85.5);

        GradeResponseDTO responseDTO = new GradeResponseDTO();
        responseDTO.setId(1);
        responseDTO.setScore(85.5);

        when(gradeService.createGrade(eq(85.5), eq(1), eq(1))).thenReturn(grade);
        when(gradeMapper.toDTO(grade)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.score").value(85.5))
                .andExpect(header().string("Location", "/api/v1/grades/1"));

        verify(gradeService).createGrade(eq(85.5), eq(1), eq(1));
    }

    @Test
    void createGrade_shouldReturnBadRequest_whenScoreNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(gradeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        GradeRequestDTO dto = new GradeRequestDTO();
        dto.setScore(null); // Double tipinde null
        dto.setStudentId(1);
        dto.setCourseId(1);

        when(gradeService.createGrade(any(), anyInt(), anyInt()))
                .thenThrow(new IllegalArgumentException("Grade score is required"));

        mockMvc.perform(post("/api/v1/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Grade score is required"));
    }

    @Test
    void updateGrade_shouldReturnUpdatedGrade() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(gradeController).build();

        GradeRequestDTO dto = new GradeRequestDTO();
        dto.setScore(90.5);
        dto.setStudentId(1);
        dto.setCourseId(1);

        Grade grade = new Grade();
        grade.setId(1);
        grade.setScore(90.5);

        GradeResponseDTO responseDTO = new GradeResponseDTO();
        responseDTO.setId(1);
        responseDTO.setScore(90.5);

        when(gradeService.updateGrade(eq(1), eq(90.5), eq(1), eq(1))).thenReturn(grade);
        when(gradeMapper.toDTO(grade)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/grades/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.score").value(90.5));

        verify(gradeService).updateGrade(eq(1), eq(90.5), eq(1), eq(1));
    }

    @Test
    void updateGrade_shouldReturnBadRequest_whenScoreNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(gradeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        GradeRequestDTO dto = new GradeRequestDTO();
        dto.setScore((double) -1); // invalid
        dto.setStudentId(1);
        dto.setCourseId(1);

        when(gradeService.updateGrade(eq(1), anyDouble(), anyInt(), anyInt()))
                .thenThrow(new IllegalArgumentException("Grade score is required"));

        mockMvc.perform(put("/api/v1/grades/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Grade score is required"));
    }

    @Test
    void deleteGrade_shouldReturnNoContent() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(gradeController).build();

        doNothing().when(gradeService).deleteGrade(1);

        mockMvc.perform(delete("/api/v1/grades/1"))
                .andExpect(status().isNoContent());

        verify(gradeService).deleteGrade(1);
    }

    @Test
    void deleteGrade_shouldReturn404_whenNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(gradeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        doThrow(new EntityNotFoundException("Grade not found"))
                .when(gradeService).deleteGrade(99);

        mockMvc.perform(delete("/api/v1/grades/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Grade not found"));

        verify(gradeService).deleteGrade(99);
    }
}