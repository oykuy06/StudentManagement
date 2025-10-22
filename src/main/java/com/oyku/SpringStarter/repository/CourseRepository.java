package com.oyku.SpringStarter.repository;

import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
