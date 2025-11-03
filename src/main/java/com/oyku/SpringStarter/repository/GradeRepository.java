package com.oyku.SpringStarter.repository;

import com.oyku.SpringStarter.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Integer> {
    List<Grade> findByStudentId(int studentId);
    List<Grade> findByCourseId(int coyrseId);
}
