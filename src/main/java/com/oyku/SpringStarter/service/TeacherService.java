package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.DTO.RequestDTO.TeacherRequestDTO;
import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.model.Teacher;
import com.oyku.SpringStarter.repository.CourseRepository;
import com.oyku.SpringStarter.repository.DepartmentRepository;
import com.oyku.SpringStarter.repository.TeacherRepository;
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

    public Optional<Teacher> getTeacherById(int id) {
        return teacherRepository.findById(id);
    }

    public Teacher addNewTeacher(TeacherRequestDTO dto) {
        Teacher teacher = new Teacher();
        teacher.setName(dto.getName());
        teacher.setTitle(dto.getTitle());

        // Department
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElse(null);
        teacher.setDepartment(department);

        // Courses
        if(dto.getCourseIds() != null){
            List<Course> courses = new ArrayList<>();
            for(int courseId : dto.getCourseIds()){
                courseRepository.findById(courseId).ifPresent(courses::add);
            }
            teacher.setCourses(courses);
        }

        return teacherRepository.save(teacher);
    }

    public Optional<Teacher> updateTeacher(int id, TeacherRequestDTO dto) {
        Optional<Teacher> optional = teacherRepository.findById(id);
        if(optional.isEmpty()) return Optional.empty();

        Teacher teacher = optional.get();
        teacher.setName(dto.getName());
        teacher.setTitle(dto.getTitle());

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElse(null);
        teacher.setDepartment(department);

        if(dto.getCourseIds() != null){
            List<Course> courses = new ArrayList<>();
            for(int courseId : dto.getCourseIds()){
                courseRepository.findById(courseId).ifPresent(courses::add);
            }
            teacher.setCourses(courses);
        }

        return Optional.of(teacherRepository.save(teacher));
    }

    public boolean deleteTeacherById(int id){
        if(!teacherRepository.existsById(id)) return false;
        teacherRepository.deleteById(id);
        return true;
    }
}
