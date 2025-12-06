package com.oyku.SpringStarter.H2Test;

import com.oyku.SpringStarter.model.*;
import com.oyku.SpringStarter.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class StudentH2Test {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private StudentProfileRepository profileRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private LibraryRepository libraryRepository;

    private Student student;
    private Department department;
    private StudentProfile profile;
    private Book book;
    private Course course;

    @BeforeEach
    void setup() {
        department = new Department();
        department.setName("Computer Science");
        department = departmentRepository.save(department);

        Teacher teacher = new Teacher();
        teacher.setName("Test Teacher");
        teacher.setDepartment(department);
        teacher = teacherRepository.save(teacher);

        profile = new StudentProfile();
        profile.setAddress("123 Main St");
        profile.setPhone("555-1234");
        profile = profileRepository.save(profile);

        course = new Course();
        course.setName("Algorithms");
        course.setTeacher(teacher);
        course = courseRepository.save(course);

        student = new Student();
        student.setName("Test Student");
        student.setDepartment(department);
        student.setProfile(profile);
        student.setCourses(new ArrayList<>(List.of(course)));

        Library library = new Library();
        library.setName("Central Library");
        library.setLocation("Main Street");
        library = libraryRepository.save(library);

        book = new Book();
        book.setTitle("Java Book");
        book.setLibrary(library);
        book.setStudent(student);

        student.getBooks().add(book);

        student = studentRepository.save(student);
    }

    @Test
    void testSaveAndFindById() {
        Optional<Student> found = studentRepository.findById(student.getId());
        assertTrue(found.isPresent());
        assertEquals("Test Student", found.get().getName());
        assertEquals("Computer Science", found.get().getDepartment().getName());
        assertEquals("123 Main St", found.get().getProfile().getAddress());
        assertEquals(1, found.get().getCourses().size());
        assertEquals(1, found.get().getBooks().size());
    }

    @Test
    void testUpdateStudent() {
        student.setName("Updated Name");
        studentRepository.save(student);

        Student updated = studentRepository.findById(student.getId()).orElseThrow();
        assertEquals("Updated Name", updated.getName());
    }

    @Test
    void testDeleteStudent() {
        int studentId = student.getId();
        studentRepository.delete(student);

        assertFalse(studentRepository.findById(studentId).isPresent());

        List<Book> books = bookRepository.findAll();
        assertTrue(books.isEmpty(), "Books should be deleted when student is deleted");
    }

    @Test
    void testFindAllStudents() {
        List<Student> students = studentRepository.findAll();
        assertFalse(students.isEmpty());
        assertTrue(students.stream().anyMatch(s -> s.getName().equals("Test Student")));
    }
}

