package com.group1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ACADEMIC_PERIOD")
public class AcademicPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int period_id;
    private String academic_year;
    private String semester;

    // Getters and Setters
    public int getPeriod_id() { return period_id; }
    public void setPeriod_id(int period_id) { this.period_id = period_id; }
    public String getAcademic_year() { return academic_year; }
    public void setAcademic_year(String academic_year) { this.academic_year = academic_year; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
}