package com.oyku.SpringStarter.mapper;

import com.oyku.SpringStarter.DTO.ResponseDTO.StudentProfileResponseDTO;
import com.oyku.SpringStarter.model.StudentProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentProfileMapperTest {

    private StudentProfileMapper profileMapper;

    @BeforeEach
    void setUp() {
        profileMapper = new StudentProfileMapper();
    }

    @Test
    void toDTO_shouldMapAllFields() {
        StudentProfile profile = new StudentProfile();
        profile.setId(1);
        profile.setAddress("Street 123");
        profile.setPhone("555-1234");

        StudentProfileResponseDTO dto = profileMapper.toDTO(profile);

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("Street 123", dto.getAddress());
        assertEquals("555-1234", dto.getPhone());
    }

    @Test
    void toDTO_shouldReturnNull_whenProfileIsNull() {
        assertNull(profileMapper.toDTO(null));
    }
}
