package com.oyku.SpringStarter.DTO.RequestDTO;

import jakarta.validation.constraints.NotBlank;

public class LibraryRequestDTO {
    @NotBlank(message = "Library name cannot be blank")
    private String name;
    private String location;

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
}
