package com.oyku.SpringStarter.DTO.ResponseDTO;

import java.util.List;

public class CourseResponseDTO {
    private int id;
    private String name;
    private TeacherResponseDTO teacher;
    private List<StudentResponseDTO> students;
    private List<GradeResponseDTO> grades;

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

    public TeacherResponseDTO getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherResponseDTO teacher) {
        this.teacher = teacher;
    }

    public List<StudentResponseDTO> getStudents() {
        return students;
    }

    public void setStudents(List<StudentResponseDTO> students) {
        this.students = students;
    }

    public List<GradeResponseDTO> getGrades() {
        return grades;
    }

    public void setGrades(List<GradeResponseDTO> grades) {
        this.grades = grades;
    }
}
