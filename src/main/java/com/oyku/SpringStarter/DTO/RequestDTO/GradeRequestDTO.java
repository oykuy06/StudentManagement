package com.oyku.SpringStarter.DTO.RequestDTO;

import jakarta.validation.constraints.*;

public class GradeRequestDTO {
    @NotNull(message = "Grade score is required")
    @Positive(message = "Grade score must be positive")
    @Min(value = 0, message = "Grade score must be at least 0")
    @Max(value = 100, message = "Grade score must be at most 100")
    private Double score;
    @NotNull(message = "Student ID is required")
    @Positive(message = "Student ID must be positive")
    private int studentId;
    @NotNull(message = "Course ID is required")
    @Positive(message = "Course ID must be positive")
    private int courseId;

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
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
