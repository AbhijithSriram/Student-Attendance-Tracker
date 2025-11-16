package com.group1.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "STUDENT_GROUP",
       uniqueConstraints = @UniqueConstraint(columnNames = {"department", "starting_year", "program_type"}))
public class StudentGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int group_id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Department department;

    @Column(nullable = false)
    private int starting_year;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProgramType program_type;

    @Column(unique = true, nullable = false)
    private String group_code; // e.g., "IT-24-UG", "ECE-25-PG"

    // Enum for Department
    public enum Department {
        IT, ECE, CSE, BME, EEE, MECH, CIVIL, CHEM
    }

    // Enum for Program Type
    public enum ProgramType {
        UG, PG
    }

    // Generate group_code from department, year, and program type
    @PrePersist
    @PreUpdate
    private void generateGroupCode() {
        if (department != null && starting_year > 0 && program_type != null) {
            // Extract last 2 digits of year (e.g., 2024 -> 24)
            String yearSuffix = String.valueOf(starting_year % 100);
            this.group_code = department.name() + "-" + yearSuffix + "-" + program_type.name();
        }
    }

    // Getters and Setters
    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
        generateGroupCode();
    }

    public int getStarting_year() {
        return starting_year;
    }

    public void setStarting_year(int starting_year) {
        this.starting_year = starting_year;
        generateGroupCode();
    }

    public ProgramType getProgram_type() {
        return program_type;
    }

    public void setProgram_type(ProgramType program_type) {
        this.program_type = program_type;
        generateGroupCode();
    }

    public String getGroup_code() {
        return group_code;
    }

    // No setter for group_code - it's auto-generated

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentGroup that = (StudentGroup) o;
        return group_id == that.group_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(group_id);
    }

    @Override
    public String toString() {
        return group_code;
    }
}
