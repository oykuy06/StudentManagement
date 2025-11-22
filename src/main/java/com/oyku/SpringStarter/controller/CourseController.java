package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.RequestDTO.CourseRequestDTO;
import com.oyku.SpringStarter.DTO.ResponseDTO.*;
import com.oyku.SpringStarter.mapper.CourseMapper;
import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.response.ApiResponse;
import com.oyku.SpringStarter.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseMapper courseMapper;

    public CourseController(CourseService courseService, CourseMapper courseMapper) {
        this.courseService = courseService;
        this.courseMapper = courseMapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponseDTO>>> getAllCourses() {
        List<CourseResponseDTO> courses = courseService.getAllCourses().stream()
                .map(courseMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Courses fetched successfully", courses)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> getCourseById(@PathVariable int id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Course fetched successfully", courseMapper.toDTO(course)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponseDTO>> createCourse(@Valid @RequestBody CourseRequestDTO dto) {
        Course created = courseService.createCourse(dto);
        CourseResponseDTO responseDTO = courseMapper.toDTO(created);

        URI location = URI.create("/api/v1/courses/" + created.getId());
        return ResponseEntity.created(location)
                .body(new ApiResponse<>(true, "Course created successfully", responseDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> updateCourse(@PathVariable int id,
                                                                       @Valid @RequestBody CourseRequestDTO dto) {
        Course updated = courseService.updateCourse(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Course updated successfully", courseMapper.toDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable int id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
