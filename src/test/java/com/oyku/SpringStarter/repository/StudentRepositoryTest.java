package com.oyku.SpringStarter.repository;

import com.oyku.SpringStarter.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Test
    void testSaveStudentWithProfileAndDepartment() {
        Department dep = new Department("Computer Science");
        departmentRepository.save(dep);

        StudentProfile profile = new StudentProfile("Istanbul", "555-1234");
        studentProfileRepository.save(profile);

        Student student = new Student("Ayşe");
        student.setDepartment(dep);
        student.setProfile(profile);

        studentRepository.save(student);

        Student found = studentRepository.findById(student.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Ayşe");
        assertThat(found.getDepartment().getName()).isEqualTo("Computer Science");
        assertThat(found.getProfile().getAddress()).isEqualTo("Istanbul");
    }
}
