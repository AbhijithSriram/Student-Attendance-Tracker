package com.group1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(reg_number, student.reg_number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reg_number);
    }
}