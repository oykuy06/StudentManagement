package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.DTO.StudentDTO;
import com.oyku.SpringStarter.model.Book;
import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.service.BookService;
import com.oyku.SpringStarter.service.CourseService;
import com.oyku.SpringStarter.service.StudentProfileService;
import com.oyku.SpringStarter.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final StudentProfileService studentProfileService;
    private final BookService bookService;
    private final CourseService courseService;

    @Autowired
    public StudentController(StudentService studentService, StudentProfileService studentProfileService,
                             BookService bookService, CourseService courseService) {
        this.studentService = studentService;
        this.studentProfileService = studentProfileService;
        this.bookService = bookService;
        this.courseService = courseService;
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) {
        Optional<Student> optionalStudent = studentService.getStudentById(id);
        return optionalStudent.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        // Profile add
        if (student.getProfile() != null && student.getProfile().getId() != 0) {
            studentProfileService.getStudentProfileById(student.getProfile().getId())
                    .ifPresent(student::setProfile);
        }

        // Book add
        List<Book> bookList = new ArrayList<>();
        if (student.getBooks() != null) {
            for (Book b : student.getBooks()) {
                bookService.getBookById(b.getId()).ifPresent(book -> {
                    book.setStudent(student); // student
                    bookList.add(book);
                });
            }
        }
        student.setBooks(bookList);

        // Course add
        List<Course> courseList = new ArrayList<>();
        if (student.getCourses() != null) {
            for (Course c : student.getCourses()) {
                courseService.getCourseById(c.getId()).ifPresent(courseList::add);
            }
        }
        student.setCourses(courseList);

        Student saved = studentService.addNewStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable int id, @RequestBody StudentDTO dto) {
        Optional<Student> optionalStudent = studentService.getStudentById(id);
        if(optionalStudent.isEmpty()) return ResponseEntity.notFound().build();

        Student s = optionalStudent.get();
        s.setName(dto.getName());
        s.setDepartment(dto.getDepartment());

        // Profile
        studentProfileService.getStudentProfileById(dto.getProfileId()).ifPresent(s::setProfile);

        // Books
        if(dto.getBookIds() != null) {
            List<Book> books = dto.getBookIds().stream()
                    .map(bookService::getBookById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .peek(book -> book.setStudent(s)) // Book’un student’ini set et
                    .toList();
            s.getBooks().clear();
            s.getBooks().addAll(books);
        }

        // Courses
        if(dto.getCourseIds() != null) {
            List<Course> courses = dto.getCourseIds().stream()
                    .map(courseService::getCourseById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            s.getCourses().clear();
            s.getCourses().addAll(courses);
        }

        studentService.updateStudent(s);
        return ResponseEntity.ok(s);
    }





    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudentById(@PathVariable int id) {
        Optional<Student> optionalStudent = studentService.getStudentById(id);
        if (optionalStudent.isEmpty()) return ResponseEntity.notFound().build();
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Student deleted with id: " + id);
    }
}
