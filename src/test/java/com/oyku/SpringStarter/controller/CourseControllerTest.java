package com.oyku.SpringStarter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oyku.SpringStarter.DTO.RequestDTO.CourseRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.CourseResponseDTO;
import com.oyku.SpringStarter.exception.GlobalExceptionHandler;
import com.oyku.SpringStarter.mapper.CourseMapper;
import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.service.CourseService;
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
public class CourseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CourseService courseService;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseController courseController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllCourses_shouldReturnCourses() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();

        Course course = new Course();
        course.setId(1);
        course.setName("Math");

        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(1);
        dto.setName("Math");

        when(courseService.getAllCourses()).thenReturn(List.of(course));
        when(courseMapper.toDTO(course)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").value("Math"));

        verify(courseService).getAllCourses();
    }

    @Test
    void getCourseById_shouldReturnCourse() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();

        Course course = new Course();
        course.setId(1);
        course.setName("Math");

        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(1);
        dto.setName("Math");

        when(courseService.getCourseById(1)).thenReturn(course);
        when(courseMapper.toDTO(course)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Math"));

        verify(courseService).getCourseById(1);
    }

    @Test
    void getCourseById_shouldReturn404_whenNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        when(courseService.getCourseById(99)).thenThrow(new EntityNotFoundException("Course not found"));

        mockMvc.perform(get("/api/v1/courses/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Course not found"));
    }

    @Test
    void createCourse_shouldReturnCreatedCourse() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();

        CourseRequestDTO requestDTO = new CourseRequestDTO();
        requestDTO.setName("Physics");

        Course course = new Course();
        course.setId(1);
        course.setName("Physics");

        CourseResponseDTO responseDTO = new CourseResponseDTO();
        responseDTO.setId(1);
        responseDTO.setName("Physics");

        when(courseService.createCourse(any(CourseRequestDTO.class))).thenReturn(course);
        when(courseMapper.toDTO(course)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Physics"))
                .andExpect(header().string("Location", "/api/v1/courses/1"));

        verify(courseService).createCourse(any(CourseRequestDTO.class));
    }

    @Test
    void createCourse_shouldReturnBadRequest_whenNameNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        CourseRequestDTO dto = new CourseRequestDTO();
        dto.setName(null);

        when(courseService.createCourse(any())).thenThrow(new IllegalArgumentException("Name cannot be null"));

        mockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Name cannot be null"));
    }

    @Test
    void updateCourse_shouldReturnUpdatedCourse() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();

        CourseRequestDTO requestDTO = new CourseRequestDTO();
        requestDTO.setName("Biology");

        Course course = new Course();
        course.setId(1);
        course.setName("Biology");

        CourseResponseDTO responseDTO = new CourseResponseDTO();
        responseDTO.setId(1);
        responseDTO.setName("Biology");

        when(courseService.updateCourse(eq(1), any(CourseRequestDTO.class))).thenReturn(course);
        when(courseMapper.toDTO(course)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/courses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Biology"));

        verify(courseService).updateCourse(eq(1), any(CourseRequestDTO.class));
    }

    @Test
    void updateCourse_shouldReturnBadRequest_whenNameNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        CourseRequestDTO dto = new CourseRequestDTO();
        dto.setName(null);

        when(courseService.updateCourse(eq(1), any())).thenThrow(new IllegalArgumentException("Name cannot be null"));

        mockMvc.perform(put("/api/v1/courses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Name cannot be null"));
    }

    @Test
    void updateCourse_shouldReturn404_whenNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        CourseRequestDTO dto = new CourseRequestDTO();
        dto.setName("Biology");

        when(courseService.updateCourse(eq(99), any())).thenThrow(new EntityNotFoundException("Course not found"));

        mockMvc.perform(put("/api/v1/courses/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Course not found"));
    }

    @Test
    void deleteCourse_shouldReturnNoContent() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();

        mockMvc.perform(delete("/api/v1/courses/1"))
                .andExpect(status().isNoContent());

        verify(courseService).deleteCourse(1);
    }

    @Test
    void deleteCourse_shouldReturn404_whenNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        doThrow(new EntityNotFoundException("Course not found"))
                .when(courseService).deleteCourse(99);

        mockMvc.perform(delete("/api/v1/courses/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Course not found"));

        verify(courseService).deleteCourse(99);
    }
}