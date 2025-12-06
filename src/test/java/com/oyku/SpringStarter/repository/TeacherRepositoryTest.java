package com.oyku.SpringStarter.repository;

import com.oyku.SpringStarter.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    void testSaveTeacher() {
        Department dep = new Department("CS");
        departmentRepository.save(dep);

        Teacher t = new Teacher("John", "Professor");
        t.setDepartment(dep);

        Teacher saved = teacherRepository.save(t);

        assertThat(saved.getId()).isGreaterThan(0);
        assertThat(saved.getDepartment().getName()).isEqualTo("CS");
    }

    @Test
    void testSaveTeacherWithCourses() {
        Department dep = new Department("CS");
        departmentRepository.save(dep);

        Teacher t = new Teacher("John", "Professor");
        t.setDepartment(dep);

        Course c1 = new Course("Math");
        c1.setTeacher(t);

        t.getCourses().add(c1);

        Teacher saved = teacherRepository.save(t);

        assertThat(saved.getCourses()).hasSize(1);
        assertThat(saved.getCourses().get(0).getName()).isEqualTo("Math");
    }

    @Test
    void testDeleteTeacher() {
        Department dep = new Department("CS");
        departmentRepository.save(dep);

        Teacher t = new Teacher("John", "Professor");
        t.setDepartment(dep);

        Teacher saved = teacherRepository.save(t);

        teacherRepository.delete(saved);

        assertThat(teacherRepository.findById(saved.getId())).isEmpty();
    }
}
