package com.group1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ATTENDANCE")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int attendance_id;
    private String status;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private ClassSession classSession;

    @ManyToOne
    @JoinColumn(name = "reg_number", nullable = false)
    private Student student;

    // Getters and Setters
    public int getAttendance_id() { return attendance_id; }
    public void setAttendance_id(int attendance_id) { this.attendance_id = attendance_id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public ClassSession getClassSession() { return classSession; }
    public void setClassSession(ClassSession classSession) { this.classSession = classSession; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
}