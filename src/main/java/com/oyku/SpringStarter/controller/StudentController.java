package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.StudentRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.*;
import com.oyku.SpringStarter.DTO.SummaryDTO.BookSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.CourseSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.DepartmentSummaryDTO;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.response.ApiResponse;
import com.oyku.SpringStarter.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getAllStudents() {
        List<StudentResponseDTO> students = studentService.getAllStudents().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Students fetched successfully", students));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> getStudentById(@PathVariable int id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Student fetched successfully", convertToResponseDTO(student)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponseDTO>> createStudent(@Valid @RequestBody StudentRequestDTO dto) {
        Student created = studentService.createStudent(dto);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Student created successfully", convertToResponseDTO(created)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> updateStudent(@PathVariable int id,
                                                                         @Valid @RequestBody StudentRequestDTO dto) {
        Student updated = studentService.updateStudent(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Student updated successfully", convertToResponseDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable int id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Student deleted successfully", null));
    }

    private StudentResponseDTO convertToResponseDTO(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setName(student.getName() != null ? student.getName() : "");

        // Department summary
        if (student.getDepartment() != null) {
            DepartmentSummaryDTO dDto = new DepartmentSummaryDTO();
            dDto.setId(student.getDepartment().getId());
            dDto.setName(student.getDepartment().getName() != null ? student.getDepartment().getName() : "");
            dto.setDepartment(dDto);
        }

        // Profile
        if (student.getProfile() != null) {
            StudentProfileResponseDTO pDto = new StudentProfileResponseDTO();
            pDto.setId(student.getProfile().getId());
            pDto.setAddress(student.getProfile().getAddress() != null ? student.getProfile().getAddress() : "");
            pDto.setPhone(student.getProfile().getPhone() != null ? student.getProfile().getPhone() : "");
            dto.setProfile(pDto);
        }

        // Books summary
        if (student.getBooks() != null) {
            dto.setBooks(student.getBooks().stream().map(b -> {
                BookSummaryDTO bDto = new BookSummaryDTO();
                bDto.setId(b.getId());
                bDto.setTitle(b.getTitle() != null ? b.getTitle() : "");
                return bDto;
            }).toList());
        } else {
            dto.setBooks(List.of());
        }

        // Courses summary
        if (student.getCourses() != null) {
            dto.setCourses(student.getCourses().stream().map(c -> {
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
