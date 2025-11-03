package com.oyku.SpringStarter.DTO.ResponseDTO;

import java.util.List;

public class StudentResponseDTO {
    private int id;
    private String name;
    private DepartmentResponseDTO department;
    private StudentProfileResponseDTO profile;
    private List<BookResponseDTO> books;
    private List<CourseResponseDTO> courses;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DepartmentResponseDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentResponseDTO department) {
        this.department = department;
    }

    public StudentProfileResponseDTO getProfile() {
        return profile;
    }

    public void setProfile(StudentProfileResponseDTO profile) {
        this.profile = profile;
    }

    public List<BookResponseDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BookResponseDTO> books) {
        this.books = books;
    }

    public List<CourseResponseDTO> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseResponseDTO> courses) {
        this.courses = courses;
    }
}
