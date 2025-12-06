package com.oyku.SpringStarter.H2Test;

import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.model.StudentProfile;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.repository.DepartmentRepository;
import com.oyku.SpringStarter.repository.StudentProfileRepository;
import com.oyku.SpringStarter.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class StudentProfileH2Test {

    @Autowired
    private StudentProfileRepository profileRepository;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    private StudentProfile profile;
    private Student student;

    @BeforeEach
    void setup() {
        Department department = new Department();
        department.setName("Computer Science");
        department = departmentRepository.save(department);

        profile = new StudentProfile();
        profile.setAddress("123 Main St");
        profile.setPhone("555-1234");
        profile = profileRepository.save(profile);

        student = new Student();
        student.setName("Student One");
        student.setDepartment(department);
        student.setProfile(profile);
        studentRepository.save(student);
    }

    @Test
    void testSaveAndFindById() {
        Optional<StudentProfile> found = profileRepository.findById(profile.getId());
        assertTrue(found.isPresent());
        assertEquals("123 Main St", found.get().getAddress());
    }

    @Test
    void testUpdateProfile() {
        profile.setPhone("999-8888");
        profileRepository.save(profile);

        StudentProfile updated = profileRepository.findById(profile.getId()).orElseThrow();
        assertEquals("999-8888", updated.getPhone());
    }

    @Test
    void testDeleteProfile() {
        int profileId = profile.getId();
        profileRepository.delete(profile);
        assertFalse(profileRepository.findById(profileId).isPresent());
    }

    @Test
    void testCascadeWithStudent() {
        student.setProfile(null);
        studentRepository.save(student);

        Student updatedStudent = studentRepository.findById(student.getId()).orElseThrow();
        assertNull(updatedStudent.getProfile());
    }
}

