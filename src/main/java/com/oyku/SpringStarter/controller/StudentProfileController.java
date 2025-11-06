package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.StudentProfileRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.StudentProfileResponseDTO;
import com.oyku.SpringStarter.model.StudentProfile;
import com.oyku.SpringStarter.response.ApiResponse;
import com.oyku.SpringStarter.service.StudentProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/profiles")
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    public StudentProfileController(StudentProfileService studentProfileService) {
        this.studentProfileService = studentProfileService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentProfileResponseDTO>>> getAllProfiles() {
        List<StudentProfileResponseDTO> profiles = studentProfileService.getAllStudentProfiles().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Profiles fetched successfully", profiles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentProfileResponseDTO>> getProfileById(@PathVariable int id) {
        StudentProfile profile = studentProfileService.getStudentProfileById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile fetched successfully", convertToResponseDTO(profile)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentProfileResponseDTO>> createProfile(@Valid @RequestBody StudentProfileRequestDTO dto) {
        StudentProfile profile = studentProfileService.addNewStudentProfile(dto);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Profile created successfully", convertToResponseDTO(profile)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentProfileResponseDTO>> updateProfile(@PathVariable int id,
                                                                                @Valid @RequestBody StudentProfileRequestDTO dto) {
        StudentProfile updated = studentProfileService.updateStudentProfileById(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile updated successfully", convertToResponseDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProfile(@PathVariable int id) {
        studentProfileService.deleteStudentProfileById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile deleted successfully", null));
    }

    private StudentProfileResponseDTO convertToResponseDTO(StudentProfile profile) {
        StudentProfileResponseDTO dto = new StudentProfileResponseDTO();
        dto.setId(profile.getId());
        dto.setAddress(profile.getAddress() != null ? profile.getAddress() : "");
        dto.setPhone(profile.getPhone() != null ? profile.getPhone() : "");
        return dto;
    }
}
