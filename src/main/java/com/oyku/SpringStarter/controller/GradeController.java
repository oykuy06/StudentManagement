package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.GradeRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.*;
import com.oyku.SpringStarter.DTO.SummaryDTO.CourseSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.StudentSummaryDTO;
import com.oyku.SpringStarter.model.Grade;
import com.oyku.SpringStarter.response.ApiResponse;
import com.oyku.SpringStarter.service.GradeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/grades")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GradeResponseDTO>>> getAllGrades() {
        List<GradeResponseDTO> response = gradeService.getAllGrades().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Grades fetched successfully", response)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GradeResponseDTO>> createGrade(@Valid @RequestBody GradeRequestDTO dto) {
        Grade created = gradeService.createGrade(dto.getScore(), dto.getStudentId(), dto.getCourseId());
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Grade created successfully", convertToResponseDTO(created)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GradeResponseDTO>> updateGrade(@PathVariable int id,
                                                                     @Valid @RequestBody GradeRequestDTO dto) {
        Grade updated = gradeService.updateGrade(id, dto.getScore(), dto.getStudentId(), dto.getCourseId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Grade updated successfully", convertToResponseDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGrade(@PathVariable int id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Grade deleted successfully", null));
    }

    private GradeResponseDTO convertToResponseDTO(Grade grade) {
        GradeResponseDTO dto = new GradeResponseDTO();
        dto.setId(grade.getId());
        dto.setScore(grade.getScore());

        // Student summary
        if (grade.getStudent() != null) {
            StudentSummaryDTO sDto = new StudentSummaryDTO();
            sDto.setId(grade.getStudent().getId());
            sDto.setName(grade.getStudent().getName() != null ? grade.getStudent().getName() : "");
            dto.setStudent(sDto);
        }

        // Course summary
        if (grade.getCourse() != null) {
            CourseSummaryDTO cDto = new CourseSummaryDTO();
            cDto.setId(grade.getCourse().getId());
            cDto.setName(grade.getCourse().getName() != null ? grade.getCourse().getName() : "");
            dto.setCourse(cDto);
        }

        return dto;
    }

}
