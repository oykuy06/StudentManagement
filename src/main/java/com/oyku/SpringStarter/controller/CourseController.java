package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.ResponseDTO.*;
import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.service.CourseService;
import com.oyku.SpringStarter.DTO.RequestDTO.CourseRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<CourseResponseDTO> getAllCourses() {
        return courseService.getAllCourses().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable int id) {
        Optional<Course> courseOpt = courseService.getCourseById(id);
        return courseOpt.map(c -> ResponseEntity.ok(convertToResponseDTO(c)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CourseResponseDTO> createCourse(@RequestBody CourseRequestDTO dto) {
        Optional<Course> courseOpt = courseService.createCourse(dto);
        return courseOpt.map(c -> ResponseEntity.status(201).body(convertToResponseDTO(c)))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable int id,
                                                          @RequestBody CourseRequestDTO dto) {
        Optional<Course> courseOpt = courseService.updateCourse(id, dto);
        return courseOpt.map(c -> ResponseEntity.ok(convertToResponseDTO(c)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable int id) {
        return courseService.deleteCourse(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private CourseResponseDTO convertToResponseDTO(Course course) {
        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName() != null ? course.getName() : "");

        // Teacher
        if (course.getTeacher() != null) {
            TeacherResponseDTO tDto = new TeacherResponseDTO();
            tDto.setId(course.getTeacher().getId());
            tDto.setName(course.getTeacher().getName() != null ? course.getTeacher().getName() : "");
            tDto.setTitle(course.getTeacher().getTitle() != null ? course.getTeacher().getTitle() : "");

            // ÖNEMLİ: Teacher'ın department bilgisini set et
            if (course.getTeacher().getDepartment() != null) {
                DepartmentResponseDTO dDto = new DepartmentResponseDTO();
                dDto.setId(course.getTeacher().getDepartment().getId());
                dDto.setName(course.getTeacher().getDepartment().getName() != null ? course.getTeacher().getDepartment().getName() : "");
                dDto.setStudents(List.of());  // Sonsuz döngüyü kır
                dDto.setTeachers(List.of());  // Sonsuz döngüyü kır
                tDto.setDepartment(dDto);
            }

            // ÖNEMLİ: Teacher'ın courses listesini boş liste olarak set et (null değil!)
            tDto.setCourses(List.of());  // Sonsuz döngüyü kır

            dto.setTeacher(tDto);
        } else {
            dto.setTeacher(null); // Teacher yoksa null bırak
        }

        // Students
        if (course.getStudents() != null && !course.getStudents().isEmpty()) {
            dto.setStudents(course.getStudents().stream().map(s -> {
                StudentResponseDTO sDto = new StudentResponseDTO();
                sDto.setId(s.getId());
                sDto.setName(s.getName() != null ? s.getName() : "");

                // ÖNEMLİ: Department bilgisini set et
                if (s.getDepartment() != null) {
                    DepartmentResponseDTO dDto = new DepartmentResponseDTO();
                    dDto.setId(s.getDepartment().getId());
                    dDto.setName(s.getDepartment().getName() != null ? s.getDepartment().getName() : "");
                    dDto.setStudents(List.of());
                    dDto.setTeachers(List.of());
                    sDto.setDepartment(dDto);
                }

                // ÖNEMLİ: Profile bilgisini set et
                if (s.getProfile() != null) {
                    StudentProfileResponseDTO pDto = new StudentProfileResponseDTO();
                    pDto.setId(s.getProfile().getId());
                    pDto.setAddress(s.getProfile().getAddress() != null ? s.getProfile().getAddress() : "");
                    pDto.setPhone(s.getProfile().getPhone() != null ? s.getProfile().getPhone() : "");
                    sDto.setProfile(pDto);
                }

                sDto.setBooks(List.of());
                sDto.setCourses(List.of());
                return sDto;
            }).toList());
        } else {
            dto.setStudents(List.of());
        }

        // Grades
        // Grades
        if (course.getGrades() != null && !course.getGrades().isEmpty()) {
            dto.setGrades(course.getGrades().stream().map(g -> {
                GradeResponseDTO gDto = new GradeResponseDTO();
                gDto.setId(g.getId());
                gDto.setScore(g.getScore());

                // Student bilgisini DOLU set et
                StudentResponseDTO sDto = new StudentResponseDTO();
                if (g.getStudent() != null) {
                    sDto.setId(g.getStudent().getId());
                    sDto.setName(g.getStudent().getName() != null ? g.getStudent().getName() : "");

                    // ÖNEMLİ: Department bilgisini set et
                    if (g.getStudent().getDepartment() != null) {
                        DepartmentResponseDTO dDto = new DepartmentResponseDTO();
                        dDto.setId(g.getStudent().getDepartment().getId());
                        dDto.setName(g.getStudent().getDepartment().getName() != null ? g.getStudent().getDepartment().getName() : "");
                        dDto.setStudents(List.of());
                        dDto.setTeachers(List.of());
                        sDto.setDepartment(dDto);
                    }

                    // ÖNEMLİ: Profile bilgisini set et
                    if (g.getStudent().getProfile() != null) {
                        StudentProfileResponseDTO pDto = new StudentProfileResponseDTO();
                        pDto.setId(g.getStudent().getProfile().getId());
                        pDto.setAddress(g.getStudent().getProfile().getAddress() != null ? g.getStudent().getProfile().getAddress() : "");
                        pDto.setPhone(g.getStudent().getProfile().getPhone() != null ? g.getStudent().getProfile().getPhone() : "");
                        sDto.setProfile(pDto);
                    }
                }
                sDto.setBooks(List.of());
                sDto.setCourses(List.of());
                gDto.setStudent(sDto);

                // Course minimal - Teacher'ı da ekle
                CourseResponseDTO cDto = new CourseResponseDTO();
                cDto.setId(course.getId());
                cDto.setName(course.getName() != null ? course.getName() : "");

                // ÖNEMLİ: Course'un teacher'ını DOLU set et
                if (course.getTeacher() != null) {
                    TeacherResponseDTO cTeacher = new TeacherResponseDTO();
                    cTeacher.setId(course.getTeacher().getId());
                    cTeacher.setName(course.getTeacher().getName() != null ? course.getTeacher().getName() : "");
                    cTeacher.setTitle(course.getTeacher().getTitle() != null ? course.getTeacher().getTitle() : "");

                    // ÖNEMLİ: Teacher'ın department'ını da ekle
                    if (course.getTeacher().getDepartment() != null) {
                        DepartmentResponseDTO tDep = new DepartmentResponseDTO();
                        tDep.setId(course.getTeacher().getDepartment().getId());
                        tDep.setName(course.getTeacher().getDepartment().getName() != null ? course.getTeacher().getDepartment().getName() : "");
                        tDep.setStudents(List.of());
                        tDep.setTeachers(List.of());
                        cTeacher.setDepartment(tDep);
                    }

                    cTeacher.setCourses(List.of());
                    cDto.setTeacher(cTeacher);
                }

                cDto.setStudents(List.of());
                cDto.setGrades(List.of());
                gDto.setCourse(cDto);

                return gDto;
            }).toList());
        } else {
            dto.setGrades(List.of());
        }

        return dto;
    }
}
