package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.model.Grade;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.repository.GradeRepository;
import com.oyku.SpringStarter.repository.StudentRepository;
import com.oyku.SpringStarter.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public GradeService(GradeRepository gradeRepository,
                        StudentRepository studentRepository,
                        CourseRepository courseRepository) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    public Grade getGradeById(int id) {
        return gradeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Grade not found with ID: " + id));
    }

    public Grade createGrade(double score, int studentId, int courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId));

        Grade grade = new Grade();
        grade.setScore(score);
        grade.setStudent(student);
        grade.setCourse(course);

        return gradeRepository.save(grade);
    }

    public Grade updateGrade(int id, double score, int studentId, int courseId) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Grade not found with ID: " + id));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId));

        grade.setScore(score);
        grade.setStudent(student);
        grade.setCourse(course);

        return gradeRepository.save(grade);
    }

    public void deleteGrade(int id) {
        if (!gradeRepository.existsById(id)) {
            throw new EntityNotFoundException("Grade not found with ID: " + id);
        }
        gradeRepository.deleteById(id);
    }

}
