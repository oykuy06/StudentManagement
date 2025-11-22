package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.GradeRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.*;
import com.oyku.SpringStarter.mapper.GradeMapper;
import com.oyku.SpringStarter.model.Grade;
import com.oyku.SpringStarter.response.ApiResponse;
import com.oyku.SpringStarter.service.GradeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/grades")
public class GradeController {

    private final GradeService gradeService;
    private final GradeMapper gradeMapper;

    public GradeController(GradeService gradeService, GradeMapper gradeMapper) {
        this.gradeService = gradeService;
        this.gradeMapper = gradeMapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GradeResponseDTO>>> getAllGrades() {
        List<GradeResponseDTO> response = gradeService.getAllGrades().stream()
                .map(gradeMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Grades fetched successfully", response)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GradeResponseDTO>> getGradeById(@PathVariable int id) {
        Grade grade = gradeService.getGradeById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Grade fetched successfully", gradeMapper.toDTO(grade)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GradeResponseDTO>> createGrade(@Valid @RequestBody GradeRequestDTO dto) {
        Grade created = gradeService.createGrade(dto.getScore(), dto.getStudentId(), dto.getCourseId());
        GradeResponseDTO responseDTO = gradeMapper.toDTO(created);

        URI location = URI.create("/api/v1/grades/" + created.getId());
        return ResponseEntity.created(location)
                .body(new ApiResponse<>(true, "Grade created successfully", responseDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GradeResponseDTO>> updateGrade(@PathVariable int id,
                                                                     @Valid @RequestBody GradeRequestDTO dto) {
        Grade updated = gradeService.updateGrade(id, dto.getScore(), dto.getStudentId(), dto.getCourseId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Grade updated successfully", gradeMapper.toDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable int id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
