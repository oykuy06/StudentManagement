package com.oyku.SpringStarter.DTO.ResponseDTO;

public class BookResponseDTO {
    private int id;
    private String title;
    private StudentResponseDTO student;
    private LibraryResponseDTO library;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public StudentResponseDTO getStudent() {
        return student;
    }

    public void setStudent(StudentResponseDTO student) {
        this.student = student;
    }

    public LibraryResponseDTO getLibrary() {
        return library;
    }

    public void setLibrary(LibraryResponseDTO library) {
        this.library = library;
    }
}
