package com.oyku.SpringStarter.DTO.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BookRequestDTO {
    @NotBlank(message = "Book title cannot be blank")
    private String title;
    @NotNull(message = "Student ID is required")
    @Positive(message = "Student ID must be positive")
    private int studentId;
    @NotNull(message = "Library ID is required")
    @Positive(message = "Library ID must be positive")
    private int libraryId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(int libraryId) {
        this.libraryId = libraryId;
    }
}
