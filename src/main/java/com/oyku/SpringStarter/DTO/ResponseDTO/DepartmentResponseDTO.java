package com.oyku.SpringStarter.DTO.ResponseDTO;

import com.oyku.SpringStarter.DTO.SummaryDTO.StudentSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.TeacherSummaryDTO;

import java.util.List;

public class DepartmentResponseDTO {
    private int id;
    private String name;
    private List<StudentSummaryDTO> students;
    private List<TeacherSummaryDTO> teachers;

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

    public List<StudentSummaryDTO> getStudents() {
        return students;
    }

    public void setStudents(List<StudentSummaryDTO> students) {
        this.students = students;
    }

    public List<TeacherSummaryDTO> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<TeacherSummaryDTO> teachers) {
        this.teachers = teachers;
    }
}
