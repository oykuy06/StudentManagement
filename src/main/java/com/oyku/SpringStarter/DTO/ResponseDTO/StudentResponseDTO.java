package com.oyku.SpringStarter.DTO.ResponseDTO;

import com.oyku.SpringStarter.DTO.SummaryDTO.BookSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.CourseSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.DepartmentSummaryDTO;

import java.util.List;

public class StudentResponseDTO {
    private int id;
    private String name;
    private DepartmentSummaryDTO department;
    private StudentProfileResponseDTO profile;
    private List<BookSummaryDTO> books;
    private List<CourseSummaryDTO> courses;

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

    public DepartmentSummaryDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentSummaryDTO department) {
        this.department = department;
    }

    public StudentProfileResponseDTO getProfile() {
        return profile;
    }

    public void setProfile(StudentProfileResponseDTO profile) {
        this.profile = profile;
    }

    public List<BookSummaryDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BookSummaryDTO> books) {
        this.books = books;
    }

    public List<CourseSummaryDTO> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseSummaryDTO> courses) {
        this.courses = courses;
    }
}
