package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.model.StudentProfile;
import com.oyku.SpringStarter.repository.*;
import com.oyku.SpringStarter.DTO.RequestDTO.StudentRequestDTO;
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

    public Optional<Student> getStudentById(int id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> createStudent(StudentRequestDTO dto) {
        Student student = new Student();
        student.setName(dto.getName());

        // Department set et ve department listesine ekle
        if (dto.getDepartmentId() > 0) {
            departmentRepository.findById(dto.getDepartmentId()).ifPresent(dep -> {
                student.setDepartment(dep); // student'a department set et
                dep.getStudents().add(student); // department'a student ekle
                departmentRepository.save(dep); // department'ı kaydet
            });
        }

        // Profile set et
        if (dto.getProfileId() > 0) {
            profileRepository.findById(dto.getProfileId())
                    .ifPresent(student::setProfile);
        }

        // Books set et
        if (dto.getBookIds() != null && !dto.getBookIds().isEmpty()) {
            List<Book> books = bookRepository.findAllById(dto.getBookIds());
            books.forEach(b -> b.setStudent(student)); // her kitaba student set et
            student.setBooks(books);
        }

        // Courses set et
        if (dto.getCourseIds() != null && !dto.getCourseIds().isEmpty()) {
            List<Course> courses = courseRepository.findAllById(dto.getCourseIds());
            student.setCourses(courses);
        }

        Student savedStudent = studentRepository.save(student);
        return Optional.of(savedStudent);
    }



    public Optional<Student> updateStudent(int id, StudentRequestDTO dto) {
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isEmpty()) return Optional.empty();

        Student student = studentOpt.get();

        if (dto.getName() != null) student.setName(dto.getName());

        // Department set et ve önceki departmanı güncelle
        if (dto.getDepartmentId() > 0) {
            departmentRepository.findById(dto.getDepartmentId()).ifPresent(dep -> {
                // Önce eski department listesinden çıkar
                Department oldDep = student.getDepartment();
                if (oldDep != null) {
                    oldDep.getStudents().remove(student);
                    departmentRepository.save(oldDep);
                }

                // Yeni department set et ve listeye ekle
                student.setDepartment(dep);
                dep.getStudents().add(student);
                departmentRepository.save(dep);
            });
        }

        // Profile set et
        if (dto.getProfileId() > 0) {
            profileRepository.findById(dto.getProfileId())
                    .ifPresent(student::setProfile);
        }

        // Books set et
        if (dto.getBookIds() != null) {
            student.getBooks().forEach(b -> b.setStudent(null)); // eski student referansını temizle
            student.getBooks().clear();

            List<Book> books = dto.getBookIds().stream()
                    .map(bookId -> bookRepository.findById(bookId).orElse(null))
                    .filter(b -> b != null)
                    .collect(Collectors.toList());

            books.forEach(b -> b.setStudent(student));
            student.getBooks().addAll(books);
        }

        // Courses set et
        if (dto.getCourseIds() != null) {
            List<Course> courses = dto.getCourseIds().stream()
                    .map(courseId -> courseRepository.findById(courseId).orElse(null))
                    .filter(c -> c != null)
                    .collect(Collectors.toList());
            student.setCourses(courses);
        }

        return Optional.of(studentRepository.save(student));
    }


    public boolean deleteStudent(int id) {
        if (!studentRepository.existsById(id)) return false;
        studentRepository.deleteById(id);
        return true;
    }
}
