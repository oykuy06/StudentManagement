package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.DTO.RequestDTO.StudentProfileRequestDTO;
import com.oyku.SpringStarter.model.StudentProfile;
import com.oyku.SpringStarter.repository.StudentProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;

    public StudentProfileService(StudentProfileRepository studentProfileRepository) {
        this.studentProfileRepository = studentProfileRepository;
    }

    public List<StudentProfile> getAllStudentProfiles() {
        return studentProfileRepository.findAll();
    }

    public Optional<StudentProfile> getStudentProfileById(int id) {
        return studentProfileRepository.findById(id);
    }

    public StudentProfile addNewStudentProfile(StudentProfileRequestDTO dto) {
        StudentProfile profile = new StudentProfile();
        profile.setAddress(dto.getAddress());
        profile.setPhone(dto.getPhone());
        return studentProfileRepository.save(profile);
    }

    public Optional<StudentProfile> updateStudentProfileById(int id, StudentProfileRequestDTO dto) {
        Optional<StudentProfile> optional = studentProfileRepository.findById(id);
        if(optional.isEmpty()) return Optional.empty();

        StudentProfile profile = optional.get();
        profile.setAddress(dto.getAddress());
        profile.setPhone(dto.getPhone());
        return Optional.of(studentProfileRepository.save(profile));
    }

    public boolean deleteStudentProfileById(int id) {
        if(!studentProfileRepository.existsById(id)) return false;
        studentProfileRepository.deleteById(id);
        return true;
    }
}
