package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.StudentRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.*;
import com.oyku.SpringStarter.mapper.StudentMapper;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.response.ApiResponse;
import com.oyku.SpringStarter.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;

    public StudentController(StudentService studentService, StudentMapper studentMapper) {
        this.studentService = studentService;
        this.studentMapper = studentMapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getAllStudents() {
        List<StudentResponseDTO> students = studentService.getAllStudents().stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Students fetched successfully", students));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> getStudentById(@PathVariable int id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Student fetched successfully", studentMapper.toDTO(student)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponseDTO>> createStudent(@Valid @RequestBody StudentRequestDTO dto) {
        Student created = studentService.createStudent(dto);
        StudentResponseDTO responseDTO = studentMapper.toDTO(created);

        URI location = URI.create("/api/v1/students/" + created.getId());
        return ResponseEntity.created(location)
                .body(new ApiResponse<>(true, "Student created successfully", responseDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> updateStudent(@PathVariable int id,
                                                                         @Valid @RequestBody StudentRequestDTO dto) {
        Student updated = studentService.updateStudent(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Student updated successfully", studentMapper.toDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable int id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
