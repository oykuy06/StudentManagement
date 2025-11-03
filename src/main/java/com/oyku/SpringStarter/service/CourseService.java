package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.model.Teacher;
import com.oyku.SpringStarter.repository.CourseRepository;
import com.oyku.SpringStarter.repository.StudentRepository;
import com.oyku.SpringStarter.repository.TeacherRepository;
import com.oyku.SpringStarter.DTO.RequestDTO.CourseRequestDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Optional<Course> getCourseById(int id) {
        return courseRepository.findById(id);
    }

    public Optional<Course> createCourse(CourseRequestDTO dto) {
        Course course = new Course();
        course.setName(dto.getName());

        // teacherId varsa öğretmeni ata
        if (dto.getTeacherId() != null) {
            Optional<Teacher> teacherOpt = teacherRepository.findById(dto.getTeacherId());
            teacherOpt.ifPresent(course::setTeacher);
        }

        // studentId listesi varsa öğrencileri ata
        if (dto.getStudentIds() != null && !dto.getStudentIds().isEmpty()) {
            List<Student> students = new ArrayList<>();
            for (Integer studentId : dto.getStudentIds()) {
                studentRepository.findById(studentId).ifPresent(students::add);
            }
            course.setStudents(students);
        }

        return Optional.of(courseRepository.save(course));
    }

    public Optional<Course> updateCourse(int id, CourseRequestDTO dto) {
        Optional<Course> courseOpt = courseRepository.findById(id);
        if (courseOpt.isEmpty()) return Optional.empty();

        Course course = courseOpt.get();
        course.setName(dto.getName());

        if (dto.getTeacherId() != null) {
            Optional<Teacher> teacherOpt = teacherRepository.findById(dto.getTeacherId());
            teacherOpt.ifPresent(course::setTeacher);
        }

        if (dto.getStudentIds() != null && !dto.getStudentIds().isEmpty()) {
            List<Student> students = new ArrayList<>();
            for (Integer studentId : dto.getStudentIds()) {
                studentRepository.findById(studentId).ifPresent(students::add);
            }
            course.setStudents(students);
        }

        return Optional.of(courseRepository.save(course));
    }

    public boolean deleteCourse(int id) {
        if (!courseRepository.existsById(id)) return false;
        courseRepository.deleteById(id);
        return true;
    }
}
