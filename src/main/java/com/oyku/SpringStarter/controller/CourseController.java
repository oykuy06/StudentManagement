package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.CourseRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.*;
import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.response.ApiResponse;
import com.oyku.SpringStarter.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponseDTO>>> getAllCourses() {
        List<CourseResponseDTO> courses = courseService.getAllCourses().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Courses retrieved successfully", courses)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> getCourseById(@PathVariable int id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Course found successfully", convertToResponseDTO(course)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponseDTO>> createCourse(@Valid @RequestBody CourseRequestDTO dto) {
        Course created = courseService.createCourse(dto);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Course created successfully", convertToResponseDTO(created)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> updateCourse(@PathVariable int id,
                                                                       @Valid @RequestBody CourseRequestDTO dto) {
        Course updated = courseService.updateCourse(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Course updated successfully", convertToResponseDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable int id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Course deleted successfully", null));
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

            if (course.getTeacher().getDepartment() != null) {
                DepartmentResponseDTO dDto = new DepartmentResponseDTO();
                dDto.setId(course.getTeacher().getDepartment().getId());
                dDto.setName(course.getTeacher().getDepartment().getName() != null
                        ? course.getTeacher().getDepartment().getName()
                        : "");
                dDto.setStudents(List.of());
                dDto.setTeachers(List.of());
                tDto.setDepartment(dDto);
            }

            tDto.setCourses(List.of());
            dto.setTeacher(tDto);
        } else {
            dto.setTeacher(null);
        }

        // Student
        if (course.getStudents() != null && !course.getStudents().isEmpty()) {
            dto.setStudents(course.getStudents().stream().map(s -> {
                StudentResponseDTO sDto = new StudentResponseDTO();
                sDto.setId(s.getId());
                sDto.setName(s.getName() != null ? s.getName() : "");

                if (s.getDepartment() != null) {
                    DepartmentResponseDTO dDto = new DepartmentResponseDTO();
                    dDto.setId(s.getDepartment().getId());
                    dDto.setName(s.getDepartment().getName() != null ? s.getDepartment().getName() : "");
                    dDto.setStudents(List.of());
                    dDto.setTeachers(List.of());
                    sDto.setDepartment(dDto);
                }

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

        // Grade
        if (course.getGrades() != null && !course.getGrades().isEmpty()) {
            dto.setGrades(course.getGrades().stream().map(g -> {
                GradeResponseDTO gDto = new GradeResponseDTO();
                gDto.setId(g.getId());
                gDto.setScore(g.getScore());

                StudentResponseDTO sDto = new StudentResponseDTO();
                if (g.getStudent() != null) {
                    sDto.setId(g.getStudent().getId());
                    sDto.setName(g.getStudent().getName() != null ? g.getStudent().getName() : "");

                    if (g.getStudent().getDepartment() != null) {
                        DepartmentResponseDTO dDto = new DepartmentResponseDTO();
                        dDto.setId(g.getStudent().getDepartment().getId());
                        dDto.setName(g.getStudent().getDepartment().getName() != null
                                ? g.getStudent().getDepartment().getName()
                                : "");
                        dDto.setStudents(List.of());
                        dDto.setTeachers(List.of());
                        sDto.setDepartment(dDto);
                    }

                    if (g.getStudent().getProfile() != null) {
                        StudentProfileResponseDTO pDto = new StudentProfileResponseDTO();
                        pDto.setId(g.getStudent().getProfile().getId());
                        pDto.setAddress(g.getStudent().getProfile().getAddress() != null
                                ? g.getStudent().getProfile().getAddress()
                                : "");
                        pDto.setPhone(g.getStudent().getProfile().getPhone() != null
                                ? g.getStudent().getProfile().getPhone()
                                : "");
                        sDto.setProfile(pDto);
                    }
                }
                sDto.setBooks(List.of());
                sDto.setCourses(List.of());
                gDto.setStudent(sDto);

                // Course
                CourseResponseDTO cDto = new CourseResponseDTO();
                cDto.setId(course.getId());
                cDto.setName(course.getName() != null ? course.getName() : "");

                if (course.getTeacher() != null) {
                    TeacherResponseDTO cTeacher = new TeacherResponseDTO();
                    cTeacher.setId(course.getTeacher().getId());
                    cTeacher.setName(course.getTeacher().getName() != null ? course.getTeacher().getName() : "");
                    cTeacher.setTitle(course.getTeacher().getTitle() != null ? course.getTeacher().getTitle() : "");

                    if (course.getTeacher().getDepartment() != null) {
                        DepartmentResponseDTO tDep = new DepartmentResponseDTO();
                        tDep.setId(course.getTeacher().getDepartment().getId());
                        tDep.setName(course.getTeacher().getDepartment().getName() != null
                                ? course.getTeacher().getDepartment().getName()
                                : "");
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
