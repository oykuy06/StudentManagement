package com.oyku.SpringStarter.model;

import jakarta.persistence.*;

@Entity
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String address;
    private String phone;

    @OneToOne(mappedBy = "profile",  fetch = FetchType.LAZY)
    private Student student;

    public StudentProfile() {}
    public StudentProfile(String address, String phone) {
        this.address = address;
        this.phone = phone;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
