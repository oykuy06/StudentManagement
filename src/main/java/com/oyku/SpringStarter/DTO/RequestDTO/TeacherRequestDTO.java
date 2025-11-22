package com.oyku.SpringStarter.DTO.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class TeacherRequestDTO {
    @NotBlank(message = "Teacher name cannot be blank")
    private String name;
    @NotBlank(message = "Teacher title cannot be blank")
    private String title;
    @NotNull(message = "Department ID is required")
    @Positive(message = "Department ID must be positive")
    private int departmentId;
    @NotNull(message = "Course IDs are required")
    private List<@Positive(message = "Course ID must be positive") Integer> courseIds;

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

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public List<Integer> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(List<Integer> courseIds) {
        this.courseIds = courseIds;
    }
}
