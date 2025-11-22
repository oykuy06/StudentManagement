package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.StudentProfileRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.StudentProfileResponseDTO;
import com.oyku.SpringStarter.mapper.StudentProfileMapper;
import com.oyku.SpringStarter.model.StudentProfile;
import com.oyku.SpringStarter.response.ApiResponse;
import com.oyku.SpringStarter.service.StudentProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/profiles")
public class StudentProfileController {

    private final StudentProfileService studentProfileService;
    private final StudentProfileMapper studentProfileMapper;

    public StudentProfileController(StudentProfileService studentProfileService, StudentProfileMapper studentProfileMapper) {
        this.studentProfileService = studentProfileService;
        this.studentProfileMapper = studentProfileMapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentProfileResponseDTO>>> getAllProfiles() {
        List<StudentProfileResponseDTO> profiles = studentProfileService.getAllStudentProfiles().stream()
                .map(studentProfileMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Profiles fetched successfully", profiles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentProfileResponseDTO>> getProfileById(@PathVariable int id) {
        StudentProfile profile = studentProfileService.getStudentProfileById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile fetched successfully", studentProfileMapper.toDTO(profile)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentProfileResponseDTO>> createProfile(@Valid @RequestBody StudentProfileRequestDTO dto) {
        StudentProfile profile = studentProfileService.addNewStudentProfile(dto);
        StudentProfileResponseDTO responseDTO = studentProfileMapper.toDTO(profile);

        URI location = URI.create("/api/v1/profiles/" + profile.getId());
        return ResponseEntity.created(location)
                .body(new ApiResponse<>(true, "Profile created successfully", responseDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentProfileResponseDTO>> updateProfile(@PathVariable int id,
                                                                                @Valid @RequestBody StudentProfileRequestDTO dto) {
        StudentProfile updated = studentProfileService.updateStudentProfileById(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile updated successfully", studentProfileMapper.toDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable int id) {
        studentProfileService.deleteStudentProfileById(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
