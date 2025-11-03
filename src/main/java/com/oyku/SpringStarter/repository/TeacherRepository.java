package com.oyku.SpringStarter.repository;

import com.oyku.SpringStarter.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
}
