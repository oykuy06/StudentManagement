package com.oyku.SpringStarter.repository;

import com.oyku.SpringStarter.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class GradeRepositoryTest {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void saveGrade_shouldPersistCorrectly() {
        Department d = new Department();
        d.setName("Science");
        d = departmentRepository.save(d);

        Student s = new Student();
        s.setName("Oyku");
        s.setDepartment(d);
        studentRepository.save(s);

        Teacher t = new Teacher();
        t.setName("John");
        t.setDepartment(d);
        teacherRepository.save(t);

        Course c = new Course("Math");
        c.setTeacher(t);
        courseRepository.save(c);

        Grade g = new Grade(90.5);
        g.setStudent(s);
        g.setCourse(c);

        Grade saved = gradeRepository.save(g);

        assertNotNull(saved.getId());
        assertEquals(90.5, saved.getScore());
        assertEquals(s.getId(), saved.getStudent().getId());
        assertEquals(c.getId(), saved.getCourse().getId());
    }
}
