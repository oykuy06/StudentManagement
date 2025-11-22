package com.oyku.SpringStarter.DTO.SummaryDTO;

public class TeacherSummaryDTO {
    private int id;
    private String name;
    private String title;
    private DepartmentSummaryDTO department;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public DepartmentSummaryDTO getDepartment() { return department; }
    public void setDepartment(DepartmentSummaryDTO department) { this.department = department; }
}
