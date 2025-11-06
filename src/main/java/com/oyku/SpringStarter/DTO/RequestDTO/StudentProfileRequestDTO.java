package com.oyku.SpringStarter.DTO.RequestDTO;

import jakarta.validation.constraints.NotBlank;

public class StudentProfileRequestDTO {
    @NotBlank(message = "Profile address cannot be blank")
    private String address;
    @NotBlank(message = "Profile phone cannot be blank")
    private String phone;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
