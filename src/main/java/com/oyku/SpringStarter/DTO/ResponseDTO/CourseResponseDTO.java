package com.oyku.SpringStarter.DTO.ResponseDTO;

import com.oyku.SpringStarter.DTO.SummaryDTO.GradeSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.StudentSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.TeacherSummaryDTO;

import java.util.List;

public class CourseResponseDTO {
    private int id;
    private String name;
    private TeacherSummaryDTO teacher;
    private List<StudentSummaryDTO> students;
    private List<GradeSummaryDTO> grades;

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

    public TeacherSummaryDTO getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherSummaryDTO teacher) {
        this.teacher = teacher;
    }

    public List<StudentSummaryDTO> getStudents() {
        return students;
    }

    public void setStudents(List<StudentSummaryDTO> students) {
        this.students = students;
    }

    public List<GradeSummaryDTO> getGrades() {
        return grades;
    }

    public void setGrades(List<GradeSummaryDTO> grades) {
        this.grades = grades;
    }
}
