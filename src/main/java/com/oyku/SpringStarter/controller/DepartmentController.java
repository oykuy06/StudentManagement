package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.DepartmentRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.*;
import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.model.Teacher;
import com.oyku.SpringStarter.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponseDTO>> getAllDepartments() {
        List<DepartmentResponseDTO> response = departmentService.getAllDepartments().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable int id) {
        Optional<Department> optional = departmentService.getDepartmentById(id);
        return optional.map(dep -> ResponseEntity.ok(convertToResponseDTO(dep)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@RequestBody DepartmentRequestDTO dto) {
        Department saved = departmentService.createDepartment(dto);
        return ResponseEntity.status(201).body(convertToResponseDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(@PathVariable int id,
                                                                  @RequestBody DepartmentRequestDTO dto) {
        Optional<Department> optional = departmentService.updateDepartment(id, dto);
        return optional.map(dep -> ResponseEntity.ok(convertToResponseDTO(dep)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable int id) {
        return departmentService.deleteDepartment(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // Helper
    private DepartmentResponseDTO convertToResponseDTO(Department dep) {
        DepartmentResponseDTO dto = new DepartmentResponseDTO();
        dto.setId(dep.getId());
        dto.setName(dep.getName() != null ? dep.getName() : "");

        // Students
        if (dep.getStudents() != null && !dep.getStudents().isEmpty()) {
            dto.setStudents(dep.getStudents().stream().map(s -> {
                StudentResponseDTO sDto = new StudentResponseDTO();
                sDto.setId(s.getId());
                sDto.setName(s.getName() != null ? s.getName() : "");

                // Department reference minimal
                DepartmentResponseDTO depDto = new DepartmentResponseDTO();
                depDto.setId(dep.getId());
                depDto.setName(dep.getName() != null ? dep.getName() : "");
                depDto.setStudents(List.of());
                depDto.setTeachers(List.of());
                sDto.setDepartment(depDto);

                // Profile
                if (s.getProfile() != null) {
                    StudentProfileResponseDTO pDto = new StudentProfileResponseDTO();
                    pDto.setId(s.getProfile().getId());
                    pDto.setAddress(s.getProfile().getAddress() != null ? s.getProfile().getAddress() : "");
                    pDto.setPhone(s.getProfile().getPhone() != null ? s.getProfile().getPhone() : "");
                    sDto.setProfile(pDto);
                }

                // Books
                if (s.getBooks() != null && !s.getBooks().isEmpty()) {
                    sDto.setBooks(s.getBooks().stream().map(b -> {
                        BookResponseDTO bDto = new BookResponseDTO();
                        bDto.setId(b.getId());
                        bDto.setTitle(b.getTitle() != null ? b.getTitle() : "");

                        // ÖNEMLİ: Student bilgisini DOLU set et
                        StudentResponseDTO bStudent = new StudentResponseDTO();
                        bStudent.setId(s.getId());
                        bStudent.setName(s.getName() != null ? s.getName() : "");

                        // Department'ı da ekle
                        if (s.getDepartment() != null) {
                            DepartmentResponseDTO bDep = new DepartmentResponseDTO();
                            bDep.setId(s.getDepartment().getId());
                            bDep.setName(s.getDepartment().getName() != null ? s.getDepartment().getName() : "");
                            bDep.setStudents(List.of());
                            bDep.setTeachers(List.of());
                            bStudent.setDepartment(bDep);
                        }

                        // Profile'ı da ekle
                        if (s.getProfile() != null) {
                            StudentProfileResponseDTO bProfile = new StudentProfileResponseDTO();
                            bProfile.setId(s.getProfile().getId());
                            bProfile.setAddress(s.getProfile().getAddress() != null ? s.getProfile().getAddress() : "");
                            bProfile.setPhone(s.getProfile().getPhone() != null ? s.getProfile().getPhone() : "");
                            bStudent.setProfile(bProfile);
                        }

                        bStudent.setBooks(List.of());
                        bStudent.setCourses(List.of());
                        bDto.setStudent(bStudent);

                        // ÖNEMLİ: Library bilgisini set et
                        if (b.getLibrary() != null) {
                            LibraryResponseDTO libDto = new LibraryResponseDTO();
                            libDto.setId(b.getLibrary().getId());
                            libDto.setName(b.getLibrary().getName() != null ? b.getLibrary().getName() : "");
                            libDto.setLocation(b.getLibrary().getLocation() != null ? b.getLibrary().getLocation() : "");
                            libDto.setBooks(List.of());
                            bDto.setLibrary(libDto);
                        }

                        return bDto;
                    }).toList());
                } else {
                    sDto.setBooks(List.of());
                }

                // Courses
                if (s.getCourses() != null && !s.getCourses().isEmpty()) {
                    sDto.setCourses(s.getCourses().stream().map(c -> {
                        CourseResponseDTO cDto = new CourseResponseDTO();
                        cDto.setId(c.getId());
                        cDto.setName(c.getName() != null ? c.getName() : "");

                        // ÖNEMLİ: Teacher bilgisini set et
                        if (c.getTeacher() != null) {
                            TeacherResponseDTO tDto = new TeacherResponseDTO();
                            tDto.setId(c.getTeacher().getId());
                            tDto.setName(c.getTeacher().getName() != null ? c.getTeacher().getName() : "");
                            tDto.setTitle(c.getTeacher().getTitle() != null ? c.getTeacher().getTitle() : "");

                            // Teacher'ın department'ını da set et
                            if (c.getTeacher().getDepartment() != null) {
                                DepartmentResponseDTO tdDto = new DepartmentResponseDTO();
                                tdDto.setId(c.getTeacher().getDepartment().getId());
                                tdDto.setName(c.getTeacher().getDepartment().getName() != null ? c.getTeacher().getDepartment().getName() : "");
                                tdDto.setStudents(List.of());
                                tdDto.setTeachers(List.of());
                                tDto.setDepartment(tdDto);
                            }

                            tDto.setCourses(List.of());
                            cDto.setTeacher(tDto);
                        }

                        cDto.setStudents(List.of());
                        cDto.setGrades(List.of());
                        return cDto;
                    }).toList());
                } else {
                    sDto.setCourses(List.of());
                }

                return sDto;
            }).toList());
        } else {
            dto.setStudents(List.of());
        }

        // Teachers
        if (dep.getTeachers() != null && !dep.getTeachers().isEmpty()) {
            dto.setTeachers(dep.getTeachers().stream().map(t -> {
                TeacherResponseDTO tDto = new TeacherResponseDTO();
                tDto.setId(t.getId());
                tDto.setName(t.getName() != null ? t.getName() : "");
                tDto.setTitle(t.getTitle() != null ? t.getTitle() : "");

                DepartmentResponseDTO tDepDto = new DepartmentResponseDTO();
                tDepDto.setId(dep.getId());
                tDepDto.setName(dep.getName() != null ? dep.getName() : "");
                tDepDto.setStudents(List.of());
                tDepDto.setTeachers(List.of());
                tDto.setDepartment(tDepDto);

                if (t.getCourses() != null && !t.getCourses().isEmpty()) {
                    tDto.setCourses(t.getCourses().stream().map(c -> {
                        CourseResponseDTO cDto = new CourseResponseDTO();
                        cDto.setId(c.getId());
                        cDto.setName(c.getName() != null ? c.getName() : "");

                        // ÖNEMLİ: Teacher bilgisini DOLU set et
                        TeacherResponseDTO cTeacher = new TeacherResponseDTO();
                        cTeacher.setId(t.getId());
                        cTeacher.setName(t.getName() != null ? t.getName() : "");
                        cTeacher.setTitle(t.getTitle() != null ? t.getTitle() : "");

                        // Department'ı da ekle
                        if (t.getDepartment() != null) {
                            DepartmentResponseDTO cDep = new DepartmentResponseDTO();
                            cDep.setId(t.getDepartment().getId());
                            cDep.setName(t.getDepartment().getName() != null ? t.getDepartment().getName() : "");
                            cDep.setStudents(List.of());
                            cDep.setTeachers(List.of());
                            cTeacher.setDepartment(cDep);
                        }

                        cTeacher.setCourses(List.of());  // Sonsuz döngüyü kır
                        cDto.setTeacher(cTeacher);

                        cDto.setStudents(List.of());
                        cDto.setGrades(List.of());
                        return cDto;
                    }).toList());
                } else {
                    tDto.setCourses(List.of());
                }

                return tDto;
            }).toList());
        } else {
            dto.setTeachers(List.of());
        }

        return dto;
    }


}
