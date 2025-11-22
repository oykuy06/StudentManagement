package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.TeacherRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.*;
import com.oyku.SpringStarter.mapper.TeacherMapper;
import com.oyku.SpringStarter.model.Teacher;
import com.oyku.SpringStarter.response.ApiResponse;
import com.oyku.SpringStarter.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;

    public TeacherController(TeacherService teacherService, TeacherMapper teacherMapper){
        this.teacherService = teacherService;
        this.teacherMapper = teacherMapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TeacherResponseDTO>>> getAllTeachers() {
        List<TeacherResponseDTO> teachers = teacherService.getAllTeachers().stream()
                .map(teacherMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Teachers fetched successfully", teachers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherResponseDTO>> getTeacherById(@PathVariable int id) {
        Teacher teacher = teacherService.getTeacherById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Teacher fetched successfully", teacherMapper.toDTO(teacher)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TeacherResponseDTO>> addTeacher(@Valid @RequestBody TeacherRequestDTO dto) {
        Teacher teacher = teacherService.addNewTeacher(dto);
        TeacherResponseDTO responseDTO = teacherMapper.toDTO(teacher);

        URI location = URI.create("/api/v1/teachers/" + teacher.getId());
        return ResponseEntity.created(location)
                .body(new ApiResponse<>(true, "Teacher created successfully", responseDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherResponseDTO>> updateTeacher(@PathVariable int id,
                                                                         @Valid @RequestBody TeacherRequestDTO dto) {
        Teacher updated = teacherService.updateTeacher(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Teacher updated successfully", teacherMapper.toDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable int id) {
        teacherService.deleteTeacherById(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
