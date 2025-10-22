package com.oyku.SpringStarter.service;

import com.oyku.SpringStarter.model.Course;
import com.oyku.SpringStarter.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository){
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourse(){
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(int id){
        return courseRepository.findById(id);
    }

    public Course addNewCourse(Course course){
        return courseRepository.save(course);
    }

    public void deleteCourse(int id){
        courseRepository.deleteById(id);
    }

    public void updateCourse(Course course){
        courseRepository.save(course);
    }

}
