package com.oyku.SpringStarter.DTO.SummaryDTO;

public class BookSummaryDTO {
    private int id;
    private String title;
    private StudentSummaryDTO student;
    private LibrarySummaryDTO library;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public StudentSummaryDTO getStudent() { return student; }
    public void setStudent(StudentSummaryDTO student) { this.student = student; }
    public LibrarySummaryDTO getLibrary() { return library; }
    public void setLibrary(LibrarySummaryDTO library) { this.library = library; }
}
