package com.oyku.SpringStarter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oyku.SpringStarter.DTO.RequestDTO.DepartmentRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.DepartmentResponseDTO;
import com.oyku.SpringStarter.exception.GlobalExceptionHandler;
import com.oyku.SpringStarter.mapper.DepartmentMapper;
import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.service.DepartmentService;
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
public class DepartmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentController departmentController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllDepartments_shouldReturnDepartments() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();

        Department dep = new Department();
        dep.setId(1);
        dep.setName("IT");

        DepartmentResponseDTO dto = new DepartmentResponseDTO();
        dto.setId(1);
        dto.setName("IT");

        when(departmentService.getAllDepartments()).thenReturn(List.of(dep));
        when(departmentMapper.toDTO(dep)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").value("IT"));

        verify(departmentService).getAllDepartments();
    }

    @Test
    void getDepartmentById_shouldReturnDepartment() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();

        Department dep = new Department();
        dep.setId(1);
        dep.setName("IT");

        DepartmentResponseDTO dto = new DepartmentResponseDTO();
        dto.setId(1);
        dto.setName("IT");

        when(departmentService.getDepartmentById(1)).thenReturn(dep);
        when(departmentMapper.toDTO(dep)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("IT"));

        verify(departmentService).getDepartmentById(1);
    }

    @Test
    void getDepartmentById_shouldReturn404_whenNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        when(departmentService.getDepartmentById(99)).thenThrow(new EntityNotFoundException("Department not found"));

        mockMvc.perform(get("/api/v1/departments/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Department not found"));
    }

    @Test
    void createDepartment_shouldReturnCreatedDepartment() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();

        DepartmentRequestDTO requestDTO = new DepartmentRequestDTO();
        requestDTO.setName("HR");

        Department dep = new Department();
        dep.setId(1);
        dep.setName("HR");

        DepartmentResponseDTO responseDTO = new DepartmentResponseDTO();
        responseDTO.setId(1);
        responseDTO.setName("HR");

        when(departmentService.createDepartment(any(DepartmentRequestDTO.class))).thenReturn(dep);
        when(departmentMapper.toDTO(dep)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("HR"))
                .andExpect(header().string("Location", "/api/v1/departments/1"));

        verify(departmentService).createDepartment(any(DepartmentRequestDTO.class));
    }

    @Test
    void createDepartment_shouldReturnBadRequest_whenNameNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        DepartmentRequestDTO dto = new DepartmentRequestDTO();
        dto.setName(null);

        when(departmentService.createDepartment(any())).thenThrow(new IllegalArgumentException("Name cannot be null"));

        mockMvc.perform(post("/api/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Name cannot be null"));
    }

    @Test
    void updateDepartment_shouldReturnUpdatedDepartment() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();

        DepartmentRequestDTO requestDTO = new DepartmentRequestDTO();
        requestDTO.setName("Finance");

        Department dep = new Department();
        dep.setId(1);
        dep.setName("Finance");

        DepartmentResponseDTO responseDTO = new DepartmentResponseDTO();
        responseDTO.setId(1);
        responseDTO.setName("Finance");

        when(departmentService.updateDepartment(eq(1), any(DepartmentRequestDTO.class))).thenReturn(dep);
        when(departmentMapper.toDTO(dep)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Finance"));

        verify(departmentService).updateDepartment(eq(1), any(DepartmentRequestDTO.class));
    }

    @Test
    void updateDepartment_shouldReturnBadRequest_whenNameNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        DepartmentRequestDTO dto = new DepartmentRequestDTO();
        dto.setName(null);

        when(departmentService.updateDepartment(eq(1), any())).thenThrow(new IllegalArgumentException("Name cannot be null"));

        mockMvc.perform(put("/api/v1/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Name cannot be null"));
    }

    @Test
    void updateDepartment_shouldReturn404_whenNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        DepartmentRequestDTO dto = new DepartmentRequestDTO();
        dto.setName("Finance");

        when(departmentService.updateDepartment(eq(99), any())).thenThrow(new EntityNotFoundException("Department not found"));

        mockMvc.perform(put("/api/v1/departments/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Department not found"));
    }

    @Test
    void deleteDepartment_shouldReturnNoContent() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();

        doNothing().when(departmentService).deleteDepartment(1);

        mockMvc.perform(delete("/api/v1/departments/1"))
                .andExpect(status().isNoContent());

        verify(departmentService).deleteDepartment(1);
    }

    @Test
    void deleteDepartment_shouldReturn404_whenNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        doThrow(new EntityNotFoundException("Department not found"))
                .when(departmentService).deleteDepartment(99);

        mockMvc.perform(delete("/api/v1/departments/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Department not found"));

        verify(departmentService).deleteDepartment(99);
    }
}