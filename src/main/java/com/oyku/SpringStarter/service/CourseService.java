package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.model.Teacher;
import com.oyku.SpringStarter.repository.CourseRepository;
import com.oyku.SpringStarter.repository.StudentRepository;
import com.oyku.SpringStarter.repository.TeacherRepository;
import com.oyku.SpringStarter.DTO.RequestDTO.CourseRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public CourseService(CourseRepository courseRepository,
                         TeacherRepository teacherRepository,
                         StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(int id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + id));
    }

    public Course createCourse(CourseRequestDTO dto) {
        Course course = new Course();
        course.setName(dto.getName());

        if (dto.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                    .orElseThrow(() -> new EntityNotFoundException("Teacher not found with ID: " + dto.getTeacherId()));
            course.setTeacher(teacher);
        }

        if (dto.getStudentIds() != null && !dto.getStudentIds().isEmpty()) {
            List<Student> students = new ArrayList<>();
            for (Integer studentId : dto.getStudentIds()) {
                Student student = studentRepository.findById(studentId)
                        .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentId));
                students.add(student);
            }
            course.setStudents(students);
        }

        return courseRepository.save(course);
    }

    public Course updateCourse(int id, CourseRequestDTO dto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + id));

        course.setName(dto.getName());

        if (dto.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                    .orElseThrow(() -> new EntityNotFoundException("Teacher not found with ID: " + dto.getTeacherId()));
            course.setTeacher(teacher);
        }

        if (dto.getStudentIds() != null && !dto.getStudentIds().isEmpty()) {
            List<Student> students = new ArrayList<>();
            for (Integer studentId : dto.getStudentIds()) {
                Student student = studentRepository.findById(studentId)
                        .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentId));
                students.add(student);
            }
            course.setStudents(students);
        }

        return courseRepository.save(course);
    }

    public boolean deleteCourse(int id) {
        if (!courseRepository.existsById(id)) return false;
        courseRepository.deleteById(id);
        return true;
    }
}
