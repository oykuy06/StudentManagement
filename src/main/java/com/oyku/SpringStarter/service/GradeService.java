package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.model.Grade;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.repository.GradeRepository;
import com.oyku.SpringStarter.repository.StudentRepository;
import com.oyku.SpringStarter.repository.CourseRepository;
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

    public Optional<Grade> getGradeById(int id) {
        return gradeRepository.findById(id);
    }

    public Optional<Grade> createGrade(double score, int studentId, int courseId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);

        if(studentOpt.isEmpty() || courseOpt.isEmpty()) return Optional.empty();

        Grade grade = new Grade();
        grade.setScore(score);
        grade.setStudent(studentOpt.get());
        grade.setCourse(courseOpt.get());

        return Optional.of(gradeRepository.save(grade));
    }

    public Optional<Grade> updateGrade(int id, double score, int studentId, int courseId) {
        Optional<Grade> gradeOpt = gradeRepository.findById(id);
        if (gradeOpt.isEmpty()) return Optional.empty();

        Grade grade = gradeOpt.get();

        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);

        if(studentOpt.isEmpty() || courseOpt.isEmpty()) return Optional.empty();

        grade.setScore(score);
        grade.setStudent(studentOpt.get());
        grade.setCourse(courseOpt.get());

        return Optional.of(gradeRepository.save(grade));
    }

    public boolean deleteGrade(int id) {
        if(!gradeRepository.existsById(id)) return false;
        gradeRepository.deleteById(id);
        return true;
    }
}
