package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.StudentRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.*;
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

        // Department
        if (student.getDepartment() != null) {
            DepartmentResponseDTO d = new DepartmentResponseDTO();
            d.setId(student.getDepartment().getId());
            d.setName(student.getDepartment().getName() != null ? student.getDepartment().getName() : "");
            d.setStudents(new ArrayList<>());
            d.setTeachers(new ArrayList<>());
            dto.setDepartment(d);
        }

        // Profile
        if (student.getProfile() != null) {
            StudentProfileResponseDTO p = new StudentProfileResponseDTO();
            p.setId(student.getProfile().getId());
            p.setAddress(student.getProfile().getAddress() != null ? student.getProfile().getAddress() : "");
            p.setPhone(student.getProfile().getPhone() != null ? student.getProfile().getPhone() : "");
            dto.setProfile(p);
        }

        // Books
        dto.setBooks(student.getBooks() != null ? student.getBooks().stream().map(b -> {
            BookResponseDTO br = new BookResponseDTO();
            br.setId(b.getId());
            br.setTitle(b.getTitle() != null ? b.getTitle() : "");

            StudentResponseDTO bStudent = new StudentResponseDTO();
            bStudent.setId(student.getId());
            bStudent.setName(student.getName() != null ? student.getName() : "");
            if (student.getDepartment() != null) {
                DepartmentResponseDTO bDep = new DepartmentResponseDTO();
                bDep.setId(student.getDepartment().getId());
                bDep.setName(student.getDepartment().getName() != null ? student.getDepartment().getName() : "");
                bDep.setStudents(new ArrayList<>());
                bDep.setTeachers(new ArrayList<>());
                bStudent.setDepartment(bDep);
            }
            if (student.getProfile() != null) {
                StudentProfileResponseDTO bProfile = new StudentProfileResponseDTO();
                bProfile.setId(student.getProfile().getId());
                bProfile.setAddress(student.getProfile().getAddress() != null ? student.getProfile().getAddress() : "");
                bProfile.setPhone(student.getProfile().getPhone() != null ? student.getProfile().getPhone() : "");
                bStudent.setProfile(bProfile);
            }
            bStudent.setBooks(new ArrayList<>());
            bStudent.setCourses(new ArrayList<>());
            br.setStudent(bStudent);

            if (b.getLibrary() != null) {
                LibraryResponseDTO lib = new LibraryResponseDTO();
                lib.setId(b.getLibrary().getId());
                lib.setName(b.getLibrary().getName() != null ? b.getLibrary().getName() : "");
                lib.setLocation(b.getLibrary().getLocation() != null ? b.getLibrary().getLocation() : "");
                lib.setBooks(new ArrayList<>());
                br.setLibrary(lib);
            }
            return br;
        }).collect(Collectors.toList()) : new ArrayList<>());

        // Courses
        dto.setCourses(student.getCourses() != null ? student.getCourses().stream().map(c -> {
            CourseResponseDTO cr = new CourseResponseDTO();
            cr.setId(c.getId());
            cr.setName(c.getName() != null ? c.getName() : "");

            if (c.getTeacher() != null) {
                TeacherResponseDTO tr = new TeacherResponseDTO();
                tr.setId(c.getTeacher().getId());
                tr.setName(c.getTeacher().getName() != null ? c.getTeacher().getName() : "");
                tr.setTitle(c.getTeacher().getTitle() != null ? c.getTeacher().getTitle() : "");
                if (c.getTeacher().getDepartment() != null) {
                    DepartmentResponseDTO td = new DepartmentResponseDTO();
                    td.setId(c.getTeacher().getDepartment().getId());
                    td.setName(c.getTeacher().getDepartment().getName() != null ? c.getTeacher().getDepartment().getName() : "");
                    td.setStudents(new ArrayList<>());
                    td.setTeachers(new ArrayList<>());
                    tr.setDepartment(td);
                }
                tr.setCourses(new ArrayList<>());
                cr.setTeacher(tr);
            }
            cr.setStudents(new ArrayList<>());
            cr.setGrades(new ArrayList<>());
            return cr;
        }).collect(Collectors.toList()) : new ArrayList<>());

        return dto;
    }
}
