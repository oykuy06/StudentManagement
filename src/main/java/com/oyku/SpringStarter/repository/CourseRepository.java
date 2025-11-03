package com.oyku.SpringStarter.repository;

import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByTeacherId(int teacherId);

}
