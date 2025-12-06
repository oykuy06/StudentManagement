package com.oyku.SpringStarter.repository;

import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.model.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    CourseRepositoryTest() {
    }

    @Test
    void saveCourse_shouldPersistCorrectly() {
        Department department = new Department();
        department.setName("Science");
        department = departmentRepository.save(department);

        Teacher teacher = new Teacher();
        teacher.setName("John");
        teacher.setDepartment(department);
        teacher = teacherRepository.save(teacher);

        Course course = new Course("Math");
        course.setTeacher(teacher);

        Course saved = courseRepository.save(course);

        assertNotNull(saved.getId());
        assertEquals("Math", saved.getName());
        assertEquals(teacher.getId(), saved.getTeacher().getId());
    }

    @Test
    void findById_shouldReturnCourse() {
        Department department = new Department();
        department.setName("Science");
        department = departmentRepository.save(department);

        Teacher teacher = new Teacher();
        teacher.setName("John");
        teacher.setDepartment(department);
        teacher = teacherRepository.save(teacher);

        Course course = new Course("Physics");
        course.setTeacher(teacher);
        Course saved = courseRepository.save(course);

        Optional<Course> found = courseRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Physics", found.get().getName());
    }

    @Test
    void findByTeacherId_shouldReturnCourses() {
        Department department = new Department();
        department.setName("Science");
        department = departmentRepository.save(department);

        Teacher teacher = new Teacher();
        teacher.setName("John");
        teacher.setDepartment(department);
        teacher = teacherRepository.save(teacher);

        Course c1 = new Course("Math");
        Course c2 = new Course("Bio");
        c1.setTeacher(teacher);
        c2.setTeacher(teacher);

        courseRepository.save(c1);
        courseRepository.save(c2);

        List<Course> list = courseRepository.findByTeacherId(teacher.getId());

        assertEquals(2, list.size());
    }
}
