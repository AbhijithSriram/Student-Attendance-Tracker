package com.group1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "COURSE_SECTION")
public class CourseSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int section_id;

    private String section_name;

    @ManyToOne
    @JoinColumn(name = "course_code", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "period_id", nullable = false)
    private AcademicPeriod academicPeriod;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Professor professor;

    // Getters and Setters
    public int getSection_id() {
        return section_id;
    }

    public void setSection_id(int section_id) {
        this.section_id = section_id;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public AcademicPeriod getAcademicPeriod() {
        return academicPeriod;
    }

    public void setAcademicPeriod(AcademicPeriod academicPeriod) {
        this.academicPeriod = academicPeriod;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}
