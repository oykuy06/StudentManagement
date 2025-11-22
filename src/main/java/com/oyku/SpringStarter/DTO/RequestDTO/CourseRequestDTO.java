package com.oyku.SpringStarter.DTO.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public class CourseRequestDTO {
    @NotBlank(message = "Course name cannot be blank")
    private String name;
    @NotNull(message = "Teacher ID is required")
    @Positive(message = "Teacher ID must be positive")
    private Integer teacherId;
    @NotNull(message = "Student IDs are required")
    private List<@Positive(message = "Student ID must be positive") Integer> studentIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public List<Integer> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<Integer> studentIds) {
        this.studentIds = studentIds;
    }
}
