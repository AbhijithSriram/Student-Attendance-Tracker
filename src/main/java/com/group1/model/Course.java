package com.group1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "COURSE")
public class Course {
    @Id
    private String course_code;
    private String course_name;
    private int credits;

    // Getters and Setters
    public String getCourse_code() { return course_code; }
    public void setCourse_code(String course_code) { this.course_code = course_code; }
    public String getCourse_name() { return course_name; }
    public void setCourse_name(String course_name) { this.course_name = course_name; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
}