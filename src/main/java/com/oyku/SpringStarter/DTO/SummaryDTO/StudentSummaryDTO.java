package com.oyku.SpringStarter.DTO.SummaryDTO;

import com.oyku.SpringStarter.DTO.ResponseDTO.StudentProfileResponseDTO;
import com.oyku.SpringStarter.model.Department;
import com.oyku.SpringStarter.model.StudentProfile;

public class StudentSummaryDTO {
    private int id;
    private String name;
    private DepartmentSummaryDTO department;
    private StudentProfileSummaryDTO profile;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public DepartmentSummaryDTO getDepartment() { return department; }
    public void setDepartment(DepartmentSummaryDTO department) { this.department = department; }
    public StudentProfileSummaryDTO getProfile() { return profile; }
    public void setProfile(StudentProfileSummaryDTO profile) { this.profile = profile; }
}
