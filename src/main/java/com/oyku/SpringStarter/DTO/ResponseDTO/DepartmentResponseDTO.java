package com.oyku.SpringStarter.DTO.ResponseDTO;

import java.util.List;

public class DepartmentResponseDTO {
    private int id;
    private String name;
    private List<StudentResponseDTO> students;
    private List<TeacherResponseDTO> teachers;

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

    public List<StudentResponseDTO> getStudents() {
        return students;
    }

    public void setStudents(List<StudentResponseDTO> students) {
        this.students = students;
    }

    public List<TeacherResponseDTO> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<TeacherResponseDTO> teachers) {
        this.teachers = teachers;
    }
}
