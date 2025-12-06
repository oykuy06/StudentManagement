package com.oyku.SpringStarter.DTO.ResponseDTO;

import com.oyku.SpringStarter.DTO.SummaryDTO.CourseSummaryDTO;
import com.oyku.SpringStarter.DTO.SummaryDTO.StudentSummaryDTO;

public class GradeResponseDTO {
    private int id;
    private Double score;
    private StudentSummaryDTO student;
    private CourseSummaryDTO course;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public StudentSummaryDTO getStudent() {
        return student;
    }

    public void setStudent(StudentSummaryDTO student) {
        this.student = student;
    }

    public CourseSummaryDTO getCourse() {
        return course;
    }

    public void setCourse(CourseSummaryDTO course) {
        this.course = course;
    }
}
