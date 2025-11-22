package com.oyku.SpringStarter.DTO.SummaryDTO;

public class GradeSummaryDTO {
    private int id;
    private double score;
    private StudentSummaryDTO student;
    private CourseSummaryDTO course;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    public StudentSummaryDTO getStudent() { return student; }
    public void setStudent(StudentSummaryDTO student) { this.student = student; }
    public CourseSummaryDTO getCourse() { return course; }
    public void setCourse(CourseSummaryDTO course) { this.course = course; }
}
