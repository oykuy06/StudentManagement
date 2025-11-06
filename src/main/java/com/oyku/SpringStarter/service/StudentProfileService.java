package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.DTO.RequestDTO.StudentProfileRequestDTO;
import com.oyku.SpringStarter.model.StudentProfile;
import com.oyku.SpringStarter.repository.StudentProfileRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public StudentProfile getStudentProfileById(int id) {
        return studentProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found with ID: " + id));
    }

    public StudentProfile addNewStudentProfile(StudentProfileRequestDTO dto) {
        StudentProfile profile = new StudentProfile();
        profile.setAddress(dto.getAddress());
        profile.setPhone(dto.getPhone());
        return studentProfileRepository.save(profile);
    }

    public StudentProfile updateStudentProfileById(int id, StudentProfileRequestDTO dto) {
        StudentProfile profile = studentProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found with ID: " + id));

        profile.setAddress(dto.getAddress());
        profile.setPhone(dto.getPhone());
        return studentProfileRepository.save(profile);
    }

    public void deleteStudentProfileById(int id) {
        if (!studentProfileRepository.existsById(id)) {
            throw new EntityNotFoundException("Profile not found with ID: " + id);
        }
        studentProfileRepository.deleteById(id);
    }

}
