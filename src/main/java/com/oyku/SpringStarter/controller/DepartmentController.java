package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.DepartmentRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.*;
import com.oyku.SpringStarter.DTO.SummaryDTO.StudentSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.TeacherSummaryDTO;
import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.response.ApiResponse;
import com.oyku.SpringStarter.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponseDTO>>> getAllDepartments() {
        List<DepartmentResponseDTO> response = departmentService.getAllDepartments().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Departments fetched successfully", response)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> getDepartmentById(@PathVariable int id) {
        Department department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Department found successfully", convertToResponseDTO(department)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> createDepartment(@Valid @RequestBody DepartmentRequestDTO dto) {
        Department saved = departmentService.createDepartment(dto);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Department created successfully", convertToResponseDTO(saved)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> updateDepartment(@PathVariable int id,
                                                                               @Valid @RequestBody DepartmentRequestDTO dto) {
        Department updated = departmentService.updateDepartment(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Department updated successfully", convertToResponseDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable int id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Department deleted successfully", null));
    }

    private DepartmentResponseDTO convertToResponseDTO(Department dep) {
        DepartmentResponseDTO dto = new DepartmentResponseDTO();
        dto.setId(dep.getId());
        dto.setName(dep.getName() != null ? dep.getName() : "");

        // Students summary
        if (dep.getStudents() != null) {
            dto.setStudents(dep.getStudents().stream().map(s -> {
                StudentSummaryDTO sDto = new StudentSummaryDTO();
                sDto.setId(s.getId());
                sDto.setName(s.getName() != null ? s.getName() : "");
                return sDto;
            }).toList());
        } else {
            dto.setStudents(List.of());
        }

        // Teachers summary
        if (dep.getTeachers() != null) {
            dto.setTeachers(dep.getTeachers().stream().map(t -> {
                TeacherSummaryDTO tDto = new TeacherSummaryDTO();
                tDto.setId(t.getId());
                tDto.setName(t.getName() != null ? t.getName() : "");
                tDto.setTitle(t.getTitle() != null ? t.getTitle() : "");
                return tDto;
            }).toList());
        } else {
            dto.setTeachers(List.of());
        }

        return dto;
    }


}
