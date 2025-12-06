package com.oyku.SpringStarter.repository;

import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.model.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    void saveDepartment_shouldPersistCorrectly() {
        Department dep = new Department("Software");
        Department saved = departmentRepository.save(dep);

        assertNotNull(saved.getId());
        assertEquals("Software", saved.getName());
    }

    @Test
    void oneToMany_students_shouldPersist() {
        Department dep = new Department("Engineering");

        Student s1 = new Student();
        s1.setName("Ayşe");

        Student s2 = new Student();
        s2.setName("Fatma");

        dep.getStudents().add(s1);
        dep.getStudents().add(s2);

        s1.setDepartment(dep);
        s2.setDepartment(dep);

        Department saved = departmentRepository.save(dep);

        assertEquals(2, saved.getStudents().size());
    }

    @Test
    void findById_shouldReturnDepartment() {
        Department dep = new Department("Math");
        Department saved = departmentRepository.save(dep);

        Optional<Department> found = departmentRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Math", found.get().getName());
    }
}
