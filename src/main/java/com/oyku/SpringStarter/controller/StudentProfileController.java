package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.StudentProfileRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.StudentProfileResponseDTO;
import com.oyku.SpringStarter.model.StudentProfile;
import com.oyku.SpringStarter.service.StudentProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/profiles")
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    public StudentProfileController(StudentProfileService studentProfileService) {
        this.studentProfileService = studentProfileService;
    }

    @GetMapping
    public List<StudentProfileResponseDTO> getAllProfiles() {
        return studentProfileService.getAllStudentProfiles().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentProfileResponseDTO> getProfileById(@PathVariable int id) {
        Optional<StudentProfile> profile = studentProfileService.getStudentProfileById(id);
        return profile.map(p -> ResponseEntity.ok(convertToResponseDTO(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StudentProfileResponseDTO> createProfile(@RequestBody StudentProfileRequestDTO dto) {
        StudentProfile profile = studentProfileService.addNewStudentProfile(dto);
        return ResponseEntity.status(201).body(convertToResponseDTO(profile));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentProfileResponseDTO> updateProfile(@PathVariable int id, @RequestBody StudentProfileRequestDTO dto) {
        Optional<StudentProfile> updated = studentProfileService.updateStudentProfileById(id, dto);
        return updated.map(p -> ResponseEntity.ok(convertToResponseDTO(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable int id) {
        return studentProfileService.deleteStudentProfileById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private StudentProfileResponseDTO convertToResponseDTO(StudentProfile profile) {
        StudentProfileResponseDTO dto = new StudentProfileResponseDTO();
        dto.setId(profile.getId());
        dto.setAddress(profile.getAddress());
        dto.setPhone(profile.getPhone());
        return dto;
    }
}
