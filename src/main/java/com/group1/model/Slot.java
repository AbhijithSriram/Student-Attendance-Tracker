package com.group1.model;

import jakarta.persistence.*;
import java.sql.Time;
import java.util.Objects;

@Entity
@Table(name = "SLOT")
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int slot_id;

    @Column(nullable = false, unique = true)
    private int slot_number; // 1-8

    @Column(nullable = false)
    private Time start_time;

    @Column(nullable = false)
    private Time end_time;

    // Getters and Setters
    public int getSlot_id() {
        return slot_id;
    }

    public void setSlot_id(int slot_id) {
        this.slot_id = slot_id;
    }

    public int getSlot_number() {
        return slot_number;
    }

    public void setSlot_number(int slot_number) {
        this.slot_number = slot_number;
    }

    public Time getStart_time() {
        return start_time;
    }

    public void setStart_time(Time start_time) {
        this.start_time = start_time;
    }

    public Time getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Time end_time) {
        this.end_time = end_time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Slot slot = (Slot) o;
        return slot_id == slot.slot_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(slot_id);
    }

    @Override
    public String toString() {
        return "Slot " + slot_number + " (" + start_time + " - " + end_time + ")";
    }
}
