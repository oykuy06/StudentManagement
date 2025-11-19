package com.oyku.SpringStarter.DTO.ResponseDTO;

import com.oyku.SpringStarter.DTO.SummaryDTO.BookSummaryDTO;

import java.util.List;

public class LibraryResponseDTO {
    private int id;
    private String name;
    private String location;
    private List<BookSummaryDTO> books;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<BookSummaryDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BookSummaryDTO> books) {
        this.books = books;
    }
}
