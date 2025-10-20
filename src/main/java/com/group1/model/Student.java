package com.group1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "STUDENT")
public class Student {
    @Id
    private String reg_number;
    private String name;

    // Getters and Setters
    public String getReg_number() { return reg_number; }
    public void setReg_number(String reg_number) { this.reg_number = reg_number; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}