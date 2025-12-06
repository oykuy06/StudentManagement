package com.oyku.SpringStarter.repository;

import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.model.StudentProfile;
import com.oyku.SpringStarter.model.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StudentProfileRepositoryTest {

    @Autowired
    private StudentProfileRepository profileRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    void testSaveProfile() {
        StudentProfile profile = new StudentProfile("Istanbul", "555");
        StudentProfile saved = profileRepository.save(profile);

        assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    void testProfileLinkedToStudent() {
        Department dep = new Department("Engineering");
        departmentRepository.save(dep);

        StudentProfile profile = new StudentProfile("X", "111");

        Student student = new Student("Ali");
        student.setDepartment(dep);
        student.setProfile(profile);

        Student saved = studentRepository.save(student);

        assertThat(saved.getProfile().getPhone()).isEqualTo("111");
    }
}
