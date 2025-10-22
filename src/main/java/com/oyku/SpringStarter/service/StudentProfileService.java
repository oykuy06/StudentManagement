package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.model.StudentProfile;
import com.oyku.SpringStarter.repository.StudentProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;

    @Autowired
    public StudentProfileService(StudentProfileRepository studentProfileRepository) {
        this.studentProfileRepository = studentProfileRepository;
    }

    public List<StudentProfile> getAllStudentProfiles() {
        return studentProfileRepository.findAll();
    }

    public Optional<StudentProfile> getStudentProfileById(int id) {
        return studentProfileRepository.findById(id);
    }

    public StudentProfile addNewStudentProfile(StudentProfile studentProfile) {
        return studentProfileRepository.save(studentProfile);
    }

    public void deleteStudentProfileById(int id) {
        studentProfileRepository.deleteById(id);
    }

    public StudentProfile updateStudentProfileById(StudentProfile studentProfile) {
        return studentProfileRepository.save(studentProfile);
    }
}
