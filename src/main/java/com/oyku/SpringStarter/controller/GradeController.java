package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.GradeRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.*;
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

        // Student
        if (grade.getStudent() != null) {
            StudentResponseDTO sDto = new StudentResponseDTO();
            sDto.setId(grade.getStudent().getId());
            sDto.setName(grade.getStudent().getName() != null ? grade.getStudent().getName() : "");

            if (grade.getStudent().getDepartment() != null) {
                DepartmentResponseDTO dDto = new DepartmentResponseDTO();
                dDto.setId(grade.getStudent().getDepartment().getId());
                dDto.setName(grade.getStudent().getDepartment().getName() != null ? grade.getStudent().getDepartment().getName() : "");
                dDto.setStudents(List.of());
                dDto.setTeachers(List.of());
                sDto.setDepartment(dDto);
            }

            if (grade.getStudent().getProfile() != null) {
                StudentProfileResponseDTO pDto = new StudentProfileResponseDTO();
                pDto.setId(grade.getStudent().getProfile().getId());
                pDto.setAddress(grade.getStudent().getProfile().getAddress() != null ? grade.getStudent().getProfile().getAddress() : "");
                pDto.setPhone(grade.getStudent().getProfile().getPhone() != null ? grade.getStudent().getProfile().getPhone() : "");
                sDto.setProfile(pDto);
            }

            sDto.setBooks(List.of());
            sDto.setCourses(List.of());
            dto.setStudent(sDto);
        } else {
            dto.setStudent(null);
        }

        // Course
        if (grade.getCourse() != null) {
            CourseResponseDTO cDto = new CourseResponseDTO();
            cDto.setId(grade.getCourse().getId());
            cDto.setName(grade.getCourse().getName() != null ? grade.getCourse().getName() : "");

            if (grade.getCourse().getTeacher() != null) {
                TeacherResponseDTO tDto = new TeacherResponseDTO();
                tDto.setId(grade.getCourse().getTeacher().getId());
                tDto.setName(grade.getCourse().getTeacher().getName() != null ? grade.getCourse().getTeacher().getName() : "");
                tDto.setTitle(grade.getCourse().getTeacher().getTitle() != null ? grade.getCourse().getTeacher().getTitle() : "");

                if (grade.getCourse().getTeacher().getDepartment() != null) {
                    DepartmentResponseDTO tdDto = new DepartmentResponseDTO();
                    tdDto.setId(grade.getCourse().getTeacher().getDepartment().getId());
                    tdDto.setName(grade.getCourse().getTeacher().getDepartment().getName() != null ? grade.getCourse().getTeacher().getDepartment().getName() : "");
                    tdDto.setStudents(List.of());
                    tdDto.setTeachers(List.of());
                    tDto.setDepartment(tdDto);
                }

                tDto.setCourses(List.of());
                cDto.setTeacher(tDto);
            }

            cDto.setStudents(List.of());
            cDto.setGrades(List.of());
            dto.setCourse(cDto);
        } else {
            dto.setCourse(null);
        }

        return dto;
    }
}
