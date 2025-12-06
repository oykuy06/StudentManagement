package com.oyku.SpringStarter.H2Test;

import com.oyku.SpringStarter.model.*;
import com.oyku.SpringStarter.repository.CourseRepository;
import com.oyku.SpringStarter.repository.DepartmentRepository;
import com.oyku.SpringStarter.repository.StudentRepository;
import com.oyku.SpringStarter.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CourseH2Test {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    private Teacher teacher;
    private Student student;
    private Course course;

    @BeforeEach
    void setup() {
        Department dep = new Department();
        dep.setName("Engineering");
        departmentRepository.save(dep);

        teacher = new Teacher();
        teacher.setName("Test Teacher");
        teacher.setDepartment(dep);
        teacher = teacherRepository.save(teacher);

        StudentProfile profile = new StudentProfile();
        profile.setAddress("Test Address");
        profile.setPhone("123456789");

        student = new Student();
        student.setName("Test Student");
        student.setDepartment(dep);
        student.setProfile(profile);
        student = studentRepository.save(student);

        course = new Course();
        course.setName("Test Course");
        course.setTeacher(teacher);

        List<Student> students = new ArrayList<>();
        students.add(student);
        course.setStudents(students);

        course = courseRepository.save(course);
    }

    @Test
    void testSaveAndFindById() {
        Optional<Course> found = courseRepository.findById(course.getId());
        assertTrue(found.isPresent());
        assertEquals("Test Course", found.get().getName());
        assertEquals("Test Teacher", found.get().getTeacher().getName());
        assertEquals(1, found.get().getStudents().size());
    }

    @Test
    void testUpdateCourse() {
        course.setName("Updated Course");
        courseRepository.save(course);

        Course updated = courseRepository.findById(course.getId()).orElseThrow();
        assertEquals("Updated Course", updated.getName());
    }

    @Test
    void testDeleteCourseAndOrphanRemoval() {
        int courseId = course.getId();
        courseRepository.delete(course);

        assertFalse(courseRepository.findById(courseId).isPresent());
        // Teacher and Student should not delete
        assertTrue(teacherRepository.findById(teacher.getId()).isPresent());
        assertTrue(studentRepository.findById(student.getId()).isPresent());
    }

    @Test
    void testCascadeRelationship() {
        Course newCourse = new Course();
        newCourse.setName("Cascade Course");
        newCourse.setTeacher(teacher);
        newCourse.setStudents(List.of(student));
        newCourse = courseRepository.save(newCourse);

        assertEquals(teacher.getId(), newCourse.getTeacher().getId());
        assertEquals(1, newCourse.getStudents().size());
    }

    @Test
    void testFindAll() {
        List<Course> courses = courseRepository.findAll();
        assertFalse(courses.isEmpty());
        assertTrue(courses.stream().anyMatch(c -> c.getName().equals("Test Course")));
    }
}

