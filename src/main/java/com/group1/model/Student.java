package com.group1.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "STUDENT")
public class Student {
    @Id
    private String reg_number;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StudentGroup.Department department;

    @Column(nullable = false)
    private int starting_year;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StudentGroup.ProgramType program_type;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private StudentGroup studentGroup;

    // Getters and Setters
    public String getReg_number() { return reg_number; }
    public void setReg_number(String reg_number) { this.reg_number = reg_number; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public StudentGroup.Department getDepartment() { return department; }
    public void setDepartment(StudentGroup.Department department) { this.department = department; }

    public int getStarting_year() { return starting_year; }
    public void setStarting_year(int starting_year) { this.starting_year = starting_year; }

    public StudentGroup.ProgramType getProgram_type() { return program_type; }
    public void setProgram_type(StudentGroup.ProgramType program_type) { this.program_type = program_type; }

    public StudentGroup getStudentGroup() { return studentGroup; }
    public void setStudentGroup(StudentGroup studentGroup) { this.studentGroup = studentGroup; }

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

    @Override
    public String toString() {
        return reg_number + " - " + name;
    }
}