package com.oyku.SpringStarter.mapper;

import com.oyku.SpringStarter.DTO.ResponseDTO.StudentProfileResponseDTO;
import com.oyku.SpringStarter.model.StudentProfile;
import org.springframework.stereotype.Component;

@Component
public class StudentProfileMapper {

    public StudentProfileResponseDTO toDTO(StudentProfile profile) {
        if (profile == null) return null;

        StudentProfileResponseDTO dto = new StudentProfileResponseDTO();
        dto.setId(profile.getId());
        dto.setAddress(profile.getAddress() != null ? profile.getAddress() : "");
        dto.setPhone(profile.getPhone() != null ? profile.getPhone() : "");
        return dto;
    }
}
