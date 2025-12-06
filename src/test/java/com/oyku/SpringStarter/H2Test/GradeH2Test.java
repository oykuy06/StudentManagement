package com.oyku.SpringStarter.H2Test;

import com.oyku.SpringStarter.model.*;
import com.oyku.SpringStarter.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class GradeH2Test {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    private Student student;
    private Course course;
    private Grade grade;

    @BeforeEach
    void setup() {
        Department dep = new Department();
        dep.setName("Engineering");
        dep = departmentRepository.save(dep);

        Teacher teacher = new Teacher();
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
        course = courseRepository.save(course);

        grade = new Grade();
        grade.setScore(90.5);
        grade.setStudent(student);
        grade.setCourse(course);
        grade = gradeRepository.save(grade);
    }

    @Test
    void testSaveAndFindById() {
        Optional<Grade> found = gradeRepository.findById(grade.getId());
        assertTrue(found.isPresent());
        assertEquals(90.5, found.get().getScore()); // doğru double karşılaştırma
        assertEquals(student.getId(), found.get().getStudent().getId());
        assertEquals(course.getId(), found.get().getCourse().getId());
    }

    @Test
    void testUpdateGrade() {
        grade.setScore(95.5);
        gradeRepository.save(grade);

        Grade updated = gradeRepository.findById(grade.getId()).orElseThrow();
        assertEquals(95.5, updated.getScore()); // doğru double karşılaştırma
    }

    @Test
    void testDeleteGrade() {
        int gradeId = grade.getId();
        gradeRepository.delete(grade);
        assertFalse(gradeRepository.findById(gradeId).isPresent());
    }

    @Test
    void testFindAllGrades() {
        List<Grade> grades = gradeRepository.findAll();
        assertFalse(grades.isEmpty());
        assertTrue(grades.stream().anyMatch(g -> g.getScore() == 90.5));
    }

    @Test
    void testCascadeAndOrphanRemoval() {
        // student ve course silinmemeli
        gradeRepository.delete(grade);
        assertTrue(studentRepository.findById(student.getId()).isPresent());
        assertTrue(courseRepository.findById(course.getId()).isPresent());
    }
}