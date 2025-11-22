package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.DepartmentRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.*;
import com.oyku.SpringStarter.mapper.DepartmentMapper;
import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.response.ApiResponse;
import com.oyku.SpringStarter.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    public DepartmentController(DepartmentService departmentService, DepartmentMapper departmentMapper) {
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponseDTO>>> getAllDepartments() {
        List<DepartmentResponseDTO> response = departmentService.getAllDepartments().stream()
                .map(departmentMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Departments fetched successfully", response)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> getDepartmentById(@PathVariable int id) {
        Department department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Department fetched successfully", departmentMapper.toDTO(department)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> createDepartment(@Valid @RequestBody DepartmentRequestDTO dto) {
        Department saved = departmentService.createDepartment(dto);
        DepartmentResponseDTO responseDTO = departmentMapper.toDTO(saved);

        // Location header ekleme
        URI location = URI.create("/api/v1/departments/" + saved.getId());
        return ResponseEntity.created(location)
                .body(new ApiResponse<>(true, "Department created successfully", responseDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> updateDepartment(@PathVariable int id,
                                                                               @Valid @RequestBody DepartmentRequestDTO dto) {
        Department updated = departmentService.updateDepartment(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Department updated successfully", departmentMapper.toDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable int id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build(); // 204
    }

}
