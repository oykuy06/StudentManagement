package com.oyku.SpringStarter.DTO.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class GradeRequestDTO {
    @NotBlank(message = "Grade score cannot be blank")
    private double score;
    @NotNull(message = "Student ID is required")
    @Positive(message = "Student ID must be positive")
    private int studentId;
    @NotNull(message = "Course ID is required")
    @Positive(message = "Course ID must be positive")
    private int courseId;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
