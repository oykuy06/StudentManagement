package com.oyku.SpringStarter.mapper;

import com.oyku.SpringStarter.DTO.ResponseDTO.CourseResponseDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.DepartmentSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.GradeSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.StudentSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.TeacherSummaryDTO;
import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.model.Grade;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.model.Teacher;
import com.oyku.SpringStarter.model.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseMapperTest {

    private CourseMapper courseMapper;

    @BeforeEach
    void setUp() {
        courseMapper = new CourseMapper();
    }

    @Test
    void toDTO_shouldMapAllFields() {
        // Teacher + Department
        Department teacherDept = new Department();
        teacherDept.setId(1);
        teacherDept.setName("Computer Science");

        Teacher teacher = new Teacher();
        teacher.setId(10);
        teacher.setName("Dr. Smith");
        teacher.setTitle("Professor");
        teacher.setDepartment(teacherDept);

        // Students
        Department studentDept = new Department();
        studentDept.setId(2);
        studentDept.setName("IT");

        Student student1 = new Student();
        student1.setId(100);
        student1.setName("Alice");
        student1.setDepartment(studentDept);

        Student student2 = new Student();
        student2.setId(101);
        student2.setName("Bob");
        student2.setDepartment(studentDept);

        // Grades
        Grade grade1 = new Grade();
        grade1.setId(1000);
        grade1.setScore(90.0);

        Grade grade2 = new Grade();
        grade2.setId(1001);
        grade2.setScore(80.0);

        // Course
        Course course = new Course();
        course.setId(1);
        course.setName("Java Programming");
        course.setTeacher(teacher);
        course.setStudents(List.of(student1, student2));
        course.setGrades(List.of(grade1, grade2));

        // Mapper
        CourseResponseDTO dto = courseMapper.toDTO(course);

        // Assertions
        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("Java Programming", dto.getName());

        // Teacher assertions
        assertNotNull(dto.getTeacher());
        assertEquals(10, dto.getTeacher().getId());
        assertEquals("Dr. Smith", dto.getTeacher().getName());
        assertEquals("Professor", dto.getTeacher().getTitle());
        assertNotNull(dto.getTeacher().getDepartment());
        assertEquals(1, dto.getTeacher().getDepartment().getId());
        assertEquals("Computer Science", dto.getTeacher().getDepartment().getName());

        // Students assertions
        assertNotNull(dto.getStudents());
        assertEquals(2, dto.getStudents().size());
        StudentSummaryDTO s1 = dto.getStudents().get(0);
        assertEquals(100, s1.getId());
        assertEquals("Alice", s1.getName());
        assertEquals(2, s1.getDepartment().getId());
        assertEquals("IT", s1.getDepartment().getName());

        StudentSummaryDTO s2 = dto.getStudents().get(1);
        assertEquals(101, s2.getId());
        assertEquals("Bob", s2.getName());

        // Grades assertions
        assertNotNull(dto.getGrades());
        assertEquals(2, dto.getGrades().size());
        GradeSummaryDTO g1 = dto.getGrades().get(0);
        assertEquals(1000, g1.getId());
        assertEquals(90.0, g1.getScore());

        GradeSummaryDTO g2 = dto.getGrades().get(1);
        assertEquals(1001, g2.getId());
        assertEquals(80.0, g2.getScore());
    }

    @Test
    void toDTO_shouldReturnNull_whenCourseIsNull() {
        assertNull(courseMapper.toDTO(null));
    }
}
