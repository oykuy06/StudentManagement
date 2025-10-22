package com.oyku.SpringStarter.repository;

import com.oyku.SpringStarter.model.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Integer> {
}
