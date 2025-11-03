package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.ResponseDTO.*;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.service.StudentService;
import com.oyku.SpringStarter.DTO.RequestDTO.StudentRequestDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<StudentResponseDTO> getAllStudents() {
        return studentService.getAllStudents().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable int id) {
        Optional<Student> studentOpt = studentService.getStudentById(id);
        return studentOpt.map(s -> ResponseEntity.ok(convertToResponseDTO(s)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@RequestBody StudentRequestDTO dto) {
        Optional<Student> studentOpt = studentService.createStudent(dto);
        return studentOpt.map(s -> ResponseEntity.status(201).body(convertToResponseDTO(s)))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(@PathVariable int id,
                                                            @RequestBody StudentRequestDTO dto) {
        Optional<Student> studentOpt = studentService.updateStudent(id, dto);
        return studentOpt.map(s -> ResponseEntity.ok(convertToResponseDTO(s)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable int id) {
        return studentService.deleteStudent(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
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
            // students ve teachers listelerini boş bırakıyoruz recursive için
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
        // Books
        if (student.getBooks() != null && !student.getBooks().isEmpty()) {
            dto.setBooks(student.getBooks().stream().map(b -> {
                BookResponseDTO br = new BookResponseDTO();
                br.setId(b.getId());
                br.setTitle(b.getTitle() != null ? b.getTitle() : "");

                // ÖNEMLİ: Book'un student bilgisini minimal set et
                StudentResponseDTO bStudent = new StudentResponseDTO();
                bStudent.setId(student.getId());
                bStudent.setName(student.getName() != null ? student.getName() : "");

                // Department'ı da ekle (minimal)
                if (student.getDepartment() != null) {
                    DepartmentResponseDTO bDep = new DepartmentResponseDTO();
                    bDep.setId(student.getDepartment().getId());
                    bDep.setName(student.getDepartment().getName() != null ? student.getDepartment().getName() : "");
                    bDep.setStudents(new ArrayList<>());
                    bDep.setTeachers(new ArrayList<>());
                    bStudent.setDepartment(bDep);
                }

                // Profile'ı da ekle
                if (student.getProfile() != null) {
                    StudentProfileResponseDTO bProfile = new StudentProfileResponseDTO();
                    bProfile.setId(student.getProfile().getId());
                    bProfile.setAddress(student.getProfile().getAddress() != null ? student.getProfile().getAddress() : "");
                    bProfile.setPhone(student.getProfile().getPhone() != null ? student.getProfile().getPhone() : "");
                    bStudent.setProfile(bProfile);
                }

                bStudent.setBooks(new ArrayList<>());   // Sonsuz döngüyü kır
                bStudent.setCourses(new ArrayList<>()); // Sonsuz döngüyü kır
                br.setStudent(bStudent);

                // ÖNEMLİ: Book'un library bilgisini set et
                if (b.getLibrary() != null) {
                    LibraryResponseDTO lib = new LibraryResponseDTO();
                    lib.setId(b.getLibrary().getId());
                    lib.setName(b.getLibrary().getName() != null ? b.getLibrary().getName() : "");
                    lib.setLocation(b.getLibrary().getLocation() != null ? b.getLibrary().getLocation() : "");
                    lib.setBooks(new ArrayList<>());  // Sonsuz döngüyü kır
                    br.setLibrary(lib);
                }

                return br;
            }).collect(Collectors.toList()));
        } else {
            dto.setBooks(new ArrayList<>());
        }

        // Courses
        if (student.getCourses() != null && !student.getCourses().isEmpty()) {
            dto.setCourses(student.getCourses().stream().map(c -> {
                CourseResponseDTO cr = new CourseResponseDTO();
                cr.setId(c.getId());
                cr.setName(c.getName() != null ? c.getName() : "");

                // ÖNEMLİ: Teacher bilgisini set et (sonsuz döngü olmaması için minimal)
                if (c.getTeacher() != null) {
                    TeacherResponseDTO tr = new TeacherResponseDTO();
                    tr.setId(c.getTeacher().getId());
                    tr.setName(c.getTeacher().getName() != null ? c.getTeacher().getName() : "");
                    tr.setTitle(c.getTeacher().getTitle() != null ? c.getTeacher().getTitle() : "");

                    // Teacher'ın department'ını da set et
                    if (c.getTeacher().getDepartment() != null) {
                        DepartmentResponseDTO td = new DepartmentResponseDTO();
                        td.setId(c.getTeacher().getDepartment().getId());
                        td.setName(c.getTeacher().getDepartment().getName() != null ? c.getTeacher().getDepartment().getName() : "");
                        td.setStudents(new ArrayList<>());  // Sonsuz döngüyü kır
                        td.setTeachers(new ArrayList<>());  // Sonsuz döngüyü kır
                        tr.setDepartment(td);
                    }

                    tr.setCourses(new ArrayList<>());  // Sonsuz döngüyü kır
                    cr.setTeacher(tr);
                }

                // recursive students ve grades boş bırakıyoruz
                cr.setStudents(new ArrayList<>());
                cr.setGrades(new ArrayList<>());
                return cr;
            }).collect(Collectors.toList()));
        } else {
            dto.setCourses(new ArrayList<>());
        }

        return dto;
    }
}
