package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.model.StudentProfile;
import com.oyku.SpringStarter.repository.*;
import com.oyku.SpringStarter.DTO.RequestDTO.StudentRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import com.oyku.SpringStarter.model.Book;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final StudentProfileRepository profileRepository;
    private final BookRepository bookRepository;
    private final CourseRepository courseRepository;


    public StudentService(StudentRepository studentRepository,
                          DepartmentRepository departmentRepository,
                          StudentProfileRepository profileRepository,
                          BookRepository bookRepository,
                          CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
        this.profileRepository = profileRepository;
        this.bookRepository = bookRepository;
        this.courseRepository = courseRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(int id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + id));
    }

    public Student createStudent(StudentRequestDTO dto) {
        Student student = new Student();
        student.setName(dto.getName());

        if (dto.getDepartmentId() > 0) {
            Department dep = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + dto.getDepartmentId()));
            student.setDepartment(dep);
            dep.getStudents().add(student);
            departmentRepository.save(dep);
        }

        if (dto.getProfileId() > 0) {
            StudentProfile profile = profileRepository.findById(dto.getProfileId())
                    .orElseThrow(() -> new EntityNotFoundException("Profile not found with ID: " + dto.getProfileId()));
            student.setProfile(profile);
        }

        if (dto.getBookIds() != null && !dto.getBookIds().isEmpty()) {
            List<Book> books = bookRepository.findAllById(dto.getBookIds());
            books.forEach(b -> b.setStudent(student));
            student.setBooks(books);
        }

        if (dto.getCourseIds() != null && !dto.getCourseIds().isEmpty()) {
            List<Course> courses = courseRepository.findAllById(dto.getCourseIds());
            student.setCourses(courses);
        }

        return studentRepository.save(student);
    }

    public Student updateStudent(int id, StudentRequestDTO dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + id));

        if (dto.getName() != null) student.setName(dto.getName());

        if (dto.getDepartmentId() > 0) {
            Department dep = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + dto.getDepartmentId()));

            Department oldDep = student.getDepartment();
            if (oldDep != null) {
                oldDep.getStudents().remove(student);
                departmentRepository.save(oldDep);
            }

            student.setDepartment(dep);
            dep.getStudents().add(student);
            departmentRepository.save(dep);
        }

        if (dto.getProfileId() > 0) {
            StudentProfile profile = profileRepository.findById(dto.getProfileId())
                    .orElseThrow(() -> new EntityNotFoundException("Profile not found with ID: " + dto.getProfileId()));
            student.setProfile(profile);
        }

        if (dto.getBookIds() != null) {
            student.getBooks().forEach(b -> b.setStudent(null));
            student.getBooks().clear();

            List<Book> books = dto.getBookIds().stream()
                    .map(bookId -> bookRepository.findById(bookId)
                            .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + bookId)))
                    .collect(Collectors.toList());

            books.forEach(b -> b.setStudent(student));
            student.getBooks().addAll(books);
        }

        if (dto.getCourseIds() != null) {
            List<Course> courses = dto.getCourseIds().stream()
                    .map(courseId -> courseRepository.findById(courseId)
                            .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId)))
                    .collect(Collectors.toList());
            student.setCourses(courses);
        }

        return studentRepository.save(student);
    }

    public void deleteStudent(int id) {
        if (!studentRepository.existsById(id)) {
            throw new EntityNotFoundException("Student not found with ID: " + id);
        }
        studentRepository.deleteById(id);
    }

}
