package com.oyku.SpringStarter.H2Test;

import com.oyku.SpringStarter.model.Teacher;
import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.repository.TeacherRepository;
import com.oyku.SpringStarter.repository.DepartmentRepository;
import com.oyku.SpringStarter.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TeacherH2Test {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Department department;
    private Teacher teacher;
    private Course course;

    @BeforeEach
    void setup() {
        department = new Department();
        department.setName("Computer Science");

        teacher = new Teacher();
        teacher.setName("John Doe");
        teacher.setTitle("Professor");
        teacher.setDepartment(department);

        department.getTeachers().add(teacher);

        department = departmentRepository.save(department);

        course = new Course();
        course.setName("Algorithms");
        course.setTeacher(teacher);
        course = courseRepository.save(course);
    }

    @Test
    void testSaveAndFindTeacher() {
        Optional<Teacher> found = teacherRepository.findById(teacher.getId());
        assertTrue(found.isPresent());
        assertEquals("John Doe", found.get().getName());
    }

    @Test
    void testUpdateTeacher() {
        teacher.setTitle("Senior Professor");
        teacherRepository.save(teacher);

        Teacher updated = teacherRepository.findById(teacher.getId()).orElseThrow();
        assertEquals("Senior Professor", updated.getTitle());
    }

    @Test
    void testDeleteTeacher() {
        int teacherId = teacher.getId();
        teacherRepository.delete(teacher);
        assertFalse(teacherRepository.findById(teacherId).isPresent());
    }

    @Test
    void testTeacherCourseRelationship() {
        List<Course> courses = courseRepository.findAll();
        assertEquals(1, courses.size());
        assertEquals("Algorithms", courses.get(0).getName());
        assertEquals("John Doe", courses.get(0).getTeacher().getName());
    }

    @Test
    void testCascadeBehaviorOnDepartment() {
        departmentRepository.delete(department);

        assertFalse(teacherRepository.findById(teacher.getId()).isPresent(),
                "Teacher should be deleted when department is deleted");
    }
}
