package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.service.CourseService;
import com.oyku.SpringStarter.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final StudentService studentService;

    @Autowired
    public CourseController(CourseService courseService, StudentService studentService) {
        this.courseService = courseService;
        this.studentService = studentService;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourse();
    }

    @GetMapping("/{id}")
    public Optional<Course> getCourseById(@PathVariable int id) {
        return courseService.getCourseById(id);
    }

    @PostMapping
    public ResponseEntity<Course> addNewCourse(@RequestBody Course course) {
        List<Student> studentsList = new ArrayList<>();
        if (course.getStudents() != null) {
            for (Student s : course.getStudents()) {
                studentService.getStudentById(s.getId()).ifPresent(studentsList::add);
            }
        }
        course.setStudents(studentsList);
        Course saved = courseService.addNewCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable int id, @RequestBody Course course) {
        Optional<Course> optionalCourse = courseService.getCourseById(id);
        if (optionalCourse.isEmpty()) return ResponseEntity.notFound().build();

        Course c = optionalCourse.get();
        c.setName(course.getName());

        List<Student> studentsList = new ArrayList<>();
        if (course.getStudents() != null) {
            for (Student s : course.getStudents()) {
                studentService.getStudentById(s.getId()).ifPresent(studentsList::add);
            }
        }
        c.setStudents(studentsList);

        courseService.updateCourse(c);
        return ResponseEntity.ok(c);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable int id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok("Course deleted with id: " + id);
    }
}
