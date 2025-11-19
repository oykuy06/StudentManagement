package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.TeacherRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.*;
import com.oyku.SpringStarter.DTO.SummaryDTO.CourseSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.DepartmentSummaryDTO;
import com.oyku.SpringStarter.model.Teacher;
import com.oyku.SpringStarter.response.ApiResponse;
import com.oyku.SpringStarter.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService){
        this.teacherService = teacherService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TeacherResponseDTO>>> getAllTeachers() {
        List<TeacherResponseDTO> teachers = teacherService.getAllTeachers().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Teachers fetched successfully", teachers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherResponseDTO>> getTeacherById(@PathVariable int id) {
        Teacher teacher = teacherService.getTeacherById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Teacher fetched successfully", convertToResponseDTO(teacher)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TeacherResponseDTO>> addTeacher(@Valid @RequestBody TeacherRequestDTO dto) {
        Teacher teacher = teacherService.addNewTeacher(dto);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Teacher created successfully", convertToResponseDTO(teacher)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherResponseDTO>> updateTeacher(@PathVariable int id,
                                                                         @Valid @RequestBody TeacherRequestDTO dto) {
        Teacher updated = teacherService.updateTeacher(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Teacher updated successfully", convertToResponseDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTeacher(@PathVariable int id) {
        teacherService.deleteTeacherById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Teacher deleted successfully", null));
    }

    private TeacherResponseDTO convertToResponseDTO(Teacher teacher) {
        TeacherResponseDTO dto = new TeacherResponseDTO();
        dto.setId(teacher.getId());
        dto.setName(teacher.getName() != null ? teacher.getName() : "");
        dto.setTitle(teacher.getTitle() != null ? teacher.getTitle() : "");

        // Department summary
        if (teacher.getDepartment() != null) {
            DepartmentSummaryDTO depDto = new DepartmentSummaryDTO();
            depDto.setId(teacher.getDepartment().getId());
            depDto.setName(teacher.getDepartment().getName() != null ? teacher.getDepartment().getName() : "");
            dto.setDepartment(depDto);
        }

        // Courses summary
        if (teacher.getCourses() != null) {
            dto.setCourses(teacher.getCourses().stream().map(c -> {
                CourseSummaryDTO cDto = new CourseSummaryDTO();
                cDto.setId(c.getId());
                cDto.setName(c.getName() != null ? c.getName() : "");
                return cDto;
            }).toList());
        } else {
            dto.setCourses(List.of());
        }

        return dto;
    }

}
