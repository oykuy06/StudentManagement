package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.DTO.RequestDTO.TeacherRequestDTO;
import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.model.Teacher;
import com.oyku.SpringStarter.repository.CourseRepository;
import com.oyku.SpringStarter.repository.DepartmentRepository;
import com.oyku.SpringStarter.repository.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;

    public TeacherService(TeacherRepository teacherRepository,
                          DepartmentRepository departmentRepository,
                          CourseRepository courseRepository) {
        this.teacherRepository = teacherRepository;
        this.departmentRepository = departmentRepository;
        this.courseRepository = courseRepository;
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacherById(int id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found with ID: " + id));
    }

    public Teacher addNewTeacher(TeacherRequestDTO dto) {
        Teacher teacher = new Teacher();
        teacher.setName(dto.getName());
        teacher.setTitle(dto.getTitle());

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + dto.getDepartmentId()));
        teacher.setDepartment(department);

        if (dto.getCourseIds() != null) {
            List<Course> courses = dto.getCourseIds().stream()
                    .map(courseId -> courseRepository.findById(courseId)
                            .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId)))
                    .toList();
            teacher.setCourses(courses);
        }

        return teacherRepository.save(teacher);
    }

    public Teacher updateTeacher(int id, TeacherRequestDTO dto) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found with ID: " + id));

        teacher.setName(dto.getName());
        teacher.setTitle(dto.getTitle());

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + dto.getDepartmentId()));
        teacher.setDepartment(department);

        if (dto.getCourseIds() != null) {
            List<Course> courses = dto.getCourseIds().stream()
                    .map(courseId -> courseRepository.findById(courseId)
                            .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId)))
                    .toList();
            teacher.setCourses(courses);
        }

        return teacherRepository.save(teacher);
    }

    public void deleteTeacherById(int id) {
        if (!teacherRepository.existsById(id)) {
            throw new EntityNotFoundException("Teacher not found with ID: " + id);
        }
        teacherRepository.deleteById(id);
    }

}
