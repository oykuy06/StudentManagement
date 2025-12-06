package com.oyku.SpringStarter.H2Test;

import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.model.Teacher;
import com.oyku.SpringStarter.repository.CourseRepository;
import com.oyku.SpringStarter.repository.DepartmentRepository;
import com.oyku.SpringStarter.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class DepartmentH2Test {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Department department;
    private Teacher teacher;
    private Course course;

    @BeforeEach
    void setup() {
        department = new Department();
        department.setName("Test Department");
        department = departmentRepository.save(department);

        teacher = new Teacher();
        teacher.setName("Test Teacher");
        teacher.setDepartment(department);
        teacher = teacherRepository.save(teacher);

        department.getTeachers().add(teacher);
        department = departmentRepository.save(department);

        course = new Course();
        course.setName("Test Course");
        course.setTeacher(teacher);
        course = courseRepository.save(course);
    }

    @Test
    void testSaveAndFindById() {
        Optional<Department> found = departmentRepository.findById(department.getId());
        assertTrue(found.isPresent());
        assertEquals("Test Department", found.get().getName());

        assertEquals(1, found.get().getTeachers().size());
        assertEquals("Test Teacher", found.get().getTeachers().get(0).getName());
    }


    @Test
    void testUpdateDepartment() {
        department.setName("Updated Department");
        departmentRepository.save(department);

        Department updated = departmentRepository.findById(department.getId()).orElseThrow();
        assertEquals("Updated Department", updated.getName());
    }

    @Test
    void testDeleteDepartmentAndOrphanRemoval() {
        int deptId = department.getId();
        departmentRepository.delete(department);

        assertFalse(departmentRepository.findById(deptId).isPresent());
        // Teacher and Course should not delete, only departman can delete
        assertTrue(teacherRepository.findById(teacher.getId()).isPresent());
        assertTrue(courseRepository.findById(course.getId()).isPresent());
    }

    @Test
    void testCascadeRelationship() {
        Department newDept = new Department();
        newDept.setName("Cascade Department");

        Teacher newTeacher = new Teacher();
        newTeacher.setName("Cascade Teacher");
        newTeacher.setDepartment(newDept);

        newDept.setTeachers(List.of(newTeacher));
        newDept = departmentRepository.save(newDept);

        assertEquals(1, newDept.getTeachers().size());
        assertEquals("Cascade Teacher", newDept.getTeachers().get(0).getName());
    }

    @Test
    void testFindAll() {
        List<Department> depts = departmentRepository.findAll();
        assertFalse(depts.isEmpty());
        assertTrue(depts.stream().anyMatch(d -> d.getName().equals("Test Department")));
    }
}

