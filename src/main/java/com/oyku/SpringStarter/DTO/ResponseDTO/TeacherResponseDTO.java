package com.oyku.SpringStarter.DTO.ResponseDTO;

import com.oyku.SpringStarter.DTO.SummaryDTO.CourseSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.DepartmentSummaryDTO;

import java.util.List;

public class TeacherResponseDTO {
    private int id;
    private String name;
    private String title;
    private DepartmentSummaryDTO department;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DepartmentSummaryDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentSummaryDTO department) {
        this.department = department;
    }

    public List<CourseSummaryDTO> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseSummaryDTO> courses) {
        this.courses = courses;
    }
}
