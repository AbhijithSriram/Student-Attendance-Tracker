package com.group1.model;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "CLASS_SESSION",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"section_id", "slot_id", "session_date"}),
           @UniqueConstraint(columnNames = {"group_id", "slot_id", "session_date"})
       })
public class ClassSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int session_id;

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    private CourseSection courseSection;

    // Denormalized for constraint enforcement
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private StudentGroup studentGroup;

    @ManyToOne
    @JoinColumn(name = "slot_id", nullable = false)
    private Slot slot;

    @Column(nullable = false)
    private Date session_date;

    private String topic_covered;

    // Getters and Setters
    public int getSession_id() { return session_id; }
    public void setSession_id(int session_id) { this.session_id = session_id; }

    public CourseSection getCourseSection() { return courseSection; }
    public void setCourseSection(CourseSection courseSection) {
        this.courseSection = courseSection;
        // Auto-sync studentGroup from courseSection
        if (courseSection != null) {
            this.studentGroup = courseSection.getStudentGroup();
        }
    }

    public StudentGroup getStudentGroup() { return studentGroup; }
    public void setStudentGroup(StudentGroup studentGroup) { this.studentGroup = studentGroup; }

    public Slot getSlot() { return slot; }
    public void setSlot(Slot slot) { this.slot = slot; }

    public Date getSession_date() { return session_date; }
    public void setSession_date(Date session_date) { this.session_date = session_date; }

    public String getTopic_covered() { return topic_covered; }
    public void setTopic_covered(String topic_covered) { this.topic_covered = topic_covered; }

    @Override
    public String toString() {
        if (courseSection != null && slot != null && session_date != null) {
            return courseSection.getSectionDisplayName() + " - " + session_date + " " + slot;
        }
        return "Session " + session_id;
    }
}