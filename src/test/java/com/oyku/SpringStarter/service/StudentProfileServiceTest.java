package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.DTO.RequestDTO.StudentProfileRequestDTO;
import com.oyku.SpringStarter.model.StudentProfile;
import com.oyku.SpringStarter.repository.StudentProfileRepository;
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
public class StudentProfileServiceTest {

    @Mock
    private StudentProfileRepository profileRepository;

    @InjectMocks
    private StudentProfileService profileService;

    private StudentProfile profile;

    @BeforeEach
    void setup() {
        profile = new StudentProfile();
        profile.setId(1);
        profile.setAddress("Test Address");
        profile.setPhone("1234567890");
    }

    @Test
    void getAllStudentProfiles_shouldReturnList() {
        when(profileRepository.findAll()).thenReturn(List.of(profile));

        List<StudentProfile> result = profileService.getAllStudentProfiles();

        assertEquals(1, result.size());
        verify(profileRepository).findAll();
    }

    @Test
    void getStudentProfileById_shouldReturnProfile_whenExists() {
        when(profileRepository.findById(1)).thenReturn(Optional.of(profile));

        StudentProfile result = profileService.getStudentProfileById(1);

        assertEquals("Test Address", result.getAddress());
        verify(profileRepository).findById(1);
    }

    @Test
    void getStudentProfileById_shouldThrowException_whenNotFound() {
        when(profileRepository.findById(2)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> profileService.getStudentProfileById(2));

        assertTrue(ex.getMessage().contains("Profile not found"));
    }

    @Test
    void addNewStudentProfile_shouldSaveAndReturn() {
        StudentProfileRequestDTO dto = new StudentProfileRequestDTO();
        dto.setAddress("New Address");
        dto.setPhone("9876543210");

        when(profileRepository.save(any(StudentProfile.class))).thenReturn(profile);

        StudentProfile result = profileService.addNewStudentProfile(dto);

        assertNotNull(result);
        verify(profileRepository).save(any(StudentProfile.class));
    }

    @Test
    void updateStudentProfileById_shouldUpdate_whenExists() {
        StudentProfileRequestDTO dto = new StudentProfileRequestDTO();
        dto.setAddress("Updated Address");
        dto.setPhone("111222333");

        when(profileRepository.findById(1)).thenReturn(Optional.of(profile));
        when(profileRepository.save(any(StudentProfile.class))).thenReturn(profile);

        StudentProfile result = profileService.updateStudentProfileById(1, dto);

        assertEquals("Updated Address", result.getAddress());
        assertEquals("111222333", result.getPhone());
        verify(profileRepository).save(any(StudentProfile.class));
    }

    @Test
    void deleteStudentProfileById_shouldDelete_whenExists() {
        when(profileRepository.existsById(1)).thenReturn(true);
        doNothing().when(profileRepository).deleteById(1);

        assertDoesNotThrow(() -> profileService.deleteStudentProfileById(1));
        verify(profileRepository).deleteById(1);
    }

    @Test
    void deleteStudentProfileById_shouldThrowException_whenNotFound() {
        when(profileRepository.existsById(2)).thenReturn(false);

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> profileService.deleteStudentProfileById(2));

        assertTrue(ex.getMessage().contains("Profile not found"));
    }
}
