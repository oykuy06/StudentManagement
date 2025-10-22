package com.oyku.SpringStarter.controller;

import com.oyku.SpringStarter.model.StudentProfile;
import com.oyku.SpringStarter.service.StudentProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student-profiles")
public class ProfileController {

    private final StudentProfileService service;

    @Autowired
    public ProfileController(StudentProfileService service) {
        this.service = service;
    }

    @GetMapping
    public List<StudentProfile> getAllStudentProfiles() {
        return service.getAllStudentProfiles();
    }

    @GetMapping("/{id}")
    public Optional<StudentProfile> getStudentProfileById(@PathVariable int id) {
        return service.getStudentProfileById(id);
    }

    @PostMapping
    public StudentProfile addNewStudentProfile(@RequestBody StudentProfile studentProfile) {
        return service.addNewStudentProfile(studentProfile);
    }

    @PutMapping("/{id}")
    public StudentProfile updateStudentProfile(@PathVariable int id, @RequestBody StudentProfile studentProfile) {
        studentProfile.setId(id);
        return service.updateStudentProfileById(studentProfile);
    }

    @DeleteMapping("/{id}")
    public void deleteStudentProfile(@PathVariable int id) {
        service.deleteStudentProfileById(id);
    }
}
