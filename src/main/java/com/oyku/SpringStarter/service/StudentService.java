package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.model.Student;
import com.oyku.SpringStarter.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository repository;

    @Autowired
    public StudentService(StudentRepository repository){
        this.repository = repository;
    }

    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    public Optional<Student> getStudentById(int id) {
        return repository.findById(id);
    }

    public Student addNewStudent(Student s) {
        return repository.save(s);
    }

    public void deleteStudent(int id) {
        repository.deleteById(id);
    }

    public void updateStudent(Student student){
        repository.save(student);
    }

}


