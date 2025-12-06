package com.oyku.SpringStarter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oyku.SpringStarter.DTO.RequestDTO.StudentProfileRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.StudentProfileResponseDTO;
import com.oyku.SpringStarter.exception.GlobalExceptionHandler;
import com.oyku.SpringStarter.mapper.StudentProfileMapper;
import com.oyku.SpringStarter.model.StudentProfile;
import com.oyku.SpringStarter.service.StudentProfileService;
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
public class StudentProfileControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentProfileService studentProfileService;

    @Mock
    private StudentProfileMapper studentProfileMapper;

    @InjectMocks
    private StudentProfileController studentProfileController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllProfiles_shouldReturnProfiles() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(studentProfileController).build();

        StudentProfile profile = new StudentProfile();
        profile.setId(1);

        StudentProfileResponseDTO dto = new StudentProfileResponseDTO();
        dto.setId(1);

        when(studentProfileService.getAllStudentProfiles()).thenReturn(List.of(profile));
        when(studentProfileMapper.toDTO(profile)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/profiles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1));

        verify(studentProfileService).getAllStudentProfiles();
    }

    @Test
    void getProfileById_shouldReturnProfile() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(studentProfileController).build();

        StudentProfile profile = new StudentProfile();
        profile.setId(1);

        StudentProfileResponseDTO dto = new StudentProfileResponseDTO();
        dto.setId(1);

        when(studentProfileService.getStudentProfileById(1)).thenReturn(profile);
        when(studentProfileMapper.toDTO(profile)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/profiles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(studentProfileService).getStudentProfileById(1);
    }

    @Test
    void getProfileById_shouldReturn404_whenNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(studentProfileController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        when(studentProfileService.getStudentProfileById(99)).thenThrow(new EntityNotFoundException("Profile not found"));

        mockMvc.perform(get("/api/v1/profiles/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Profile not found"));
    }

    @Test
    void createProfile_shouldReturnCreatedProfile() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(studentProfileController).build();

        StudentProfileRequestDTO requestDTO = new StudentProfileRequestDTO();

        StudentProfile profile = new StudentProfile();
        profile.setId(1);

        StudentProfileResponseDTO dto = new StudentProfileResponseDTO();
        dto.setId(1);

        when(studentProfileService.addNewStudentProfile(any(StudentProfileRequestDTO.class))).thenReturn(profile);
        when(studentProfileMapper.toDTO(profile)).thenReturn(dto);

        mockMvc.perform(post("/api/v1/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(header().string("Location", "/api/v1/profiles/1"));

        verify(studentProfileService).addNewStudentProfile(any(StudentProfileRequestDTO.class));
    }

    @Test
    void updateProfile_shouldReturnUpdatedProfile() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(studentProfileController).build();

        StudentProfileRequestDTO requestDTO = new StudentProfileRequestDTO();

        StudentProfile profile = new StudentProfile();
        profile.setId(1);

        StudentProfileResponseDTO dto = new StudentProfileResponseDTO();
        dto.setId(1);

        when(studentProfileService.updateStudentProfileById(eq(1), any(StudentProfileRequestDTO.class))).thenReturn(profile);
        when(studentProfileMapper.toDTO(profile)).thenReturn(dto);

        mockMvc.perform(put("/api/v1/profiles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(studentProfileService).updateStudentProfileById(eq(1), any(StudentProfileRequestDTO.class));
    }

    @Test
    void deleteProfile_shouldReturnNoContent() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(studentProfileController).build();

        doNothing().when(studentProfileService).deleteStudentProfileById(1);

        mockMvc.perform(delete("/api/v1/profiles/1"))
                .andExpect(status().isNoContent());

        verify(studentProfileService).deleteStudentProfileById(1);
    }

    @Test
    void deleteProfile_shouldReturn404_whenNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(studentProfileController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        doThrow(new EntityNotFoundException("Profile not found"))
                .when(studentProfileService).deleteStudentProfileById(99);

        mockMvc.perform(delete("/api/v1/profiles/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Profile not found"));

        verify(studentProfileService).deleteStudentProfileById(99);
    }
}