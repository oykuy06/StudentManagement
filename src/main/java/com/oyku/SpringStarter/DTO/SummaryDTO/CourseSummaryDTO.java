package com.oyku.SpringStarter.DTO.SummaryDTO;

public class CourseSummaryDTO {
    private int id;
    private String name;
    private TeacherSummaryDTO teacher;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public TeacherSummaryDTO getTeacher() { return teacher; }
    public void setTeacher(TeacherSummaryDTO teacher) { this.teacher = teacher; }
}
