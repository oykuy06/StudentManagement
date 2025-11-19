package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.CourseRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.*;
import com.oyku.SpringStarter.DTO.SummaryDTO.DepartmentSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.GradeSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.StudentSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.TeacherSummaryDTO;
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

        // Teacher summary
        if (course.getTeacher() != null) {
            TeacherSummaryDTO t = new TeacherSummaryDTO();
            t.setId(course.getTeacher().getId());
            t.setName(course.getTeacher().getName() != null ? course.getTeacher().getName() : "");
            t.setTitle(course.getTeacher().getTitle() != null ? course.getTeacher().getTitle() : "");

            if (course.getTeacher().getDepartment() != null) {
                DepartmentSummaryDTO d = new DepartmentSummaryDTO();
                d.setId(course.getTeacher().getDepartment().getId());
                d.setName(course.getTeacher().getDepartment().getName() != null ? course.getTeacher().getDepartment().getName() : "");
                t.setDepartment(d);
            }

            dto.setTeacher(t);
        }

        // Students summary
        if (course.getStudents() != null) {
            dto.setStudents(course.getStudents().stream().map(s -> {
                StudentSummaryDTO sDto = new StudentSummaryDTO();
                sDto.setId(s.getId());
                sDto.setName(s.getName() != null ? s.getName() : "");
                if (s.getDepartment() != null) {
                    DepartmentSummaryDTO d = new DepartmentSummaryDTO();
                    d.setId(s.getDepartment().getId());
                    d.setName(s.getDepartment().getName() != null ? s.getDepartment().getName() : "");
                    sDto.setDepartment(d);
                }
                return sDto;
            }).toList());
        }

        // Grades summary using GradeSummaryDTO
        if (course.getGrades() != null) {
            dto.setGrades(course.getGrades().stream().map(g -> {
                GradeSummaryDTO gDto = new GradeSummaryDTO();
                gDto.setId(g.getId());
                gDto.setScore(g.getScore());
                // istersen öğrenci adı vs. ekleyebilirsin, ama summary olduğu için minimal bırak
                return gDto;
            }).toList());
        }


        return dto;
    }

}
