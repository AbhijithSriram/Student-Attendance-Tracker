package com.group1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "COURSE_SECTION",
       uniqueConstraints = @UniqueConstraint(columnNames = {"group_id", "section_letter", "course_code", "period_id"}))
public class CourseSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int section_id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private StudentGroup studentGroup;

    @Column(nullable = false, length = 1)
    private String section_letter; // A, B, C, etc.

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

    public StudentGroup getStudentGroup() {
        return studentGroup;
    }

    public void setStudentGroup(StudentGroup studentGroup) {
        this.studentGroup = studentGroup;
    }

    public String getSection_letter() {
        return section_letter;
    }

    public void setSection_letter(String section_letter) {
        this.section_letter = section_letter;
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

    // Helper method to get section display name
    public String getSectionDisplayName() {
        if (studentGroup != null && course != null) {
            return studentGroup.getGroup_code() + " Section " + section_letter + " - " + course.getCourse_name();
        }
        return "Section " + section_letter;
    }

    @Override
    public String toString() {
        return getSectionDisplayName();
    }
}
