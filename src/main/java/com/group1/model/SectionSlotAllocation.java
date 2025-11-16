package com.group1.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "SECTION_SLOT_ALLOCATION",
       uniqueConstraints = @UniqueConstraint(columnNames = {"section_id", "slot_id", "day_of_week"}))
public class SectionSlotAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int allocation_id;

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    private CourseSection courseSection;

    @ManyToOne
    @JoinColumn(name = "slot_id", nullable = false)
    private Slot slot;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek day_of_week;

    // Enum for Day of Week
    public enum DayOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
    }

    // Getters and Setters
    public int getAllocation_id() {
        return allocation_id;
    }

    public void setAllocation_id(int allocation_id) {
        this.allocation_id = allocation_id;
    }

    public CourseSection getCourseSection() {
        return courseSection;
    }

    public void setCourseSection(CourseSection courseSection) {
        this.courseSection = courseSection;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public DayOfWeek getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(DayOfWeek day_of_week) {
        this.day_of_week = day_of_week;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionSlotAllocation that = (SectionSlotAllocation) o;
        return allocation_id == that.allocation_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(allocation_id);
    }

    @Override
    public String toString() {
        return day_of_week + " - " + slot;
    }
}
