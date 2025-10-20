package com.group1.model;

import jakarta.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "CLASS_SESSION")
public class ClassSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int session_id;

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    private CourseSection courseSection;

    private Date session_date;
    private Time start_time;
    private Time end_time;
    private String topic_covered;

    // Getters and Setters
    public int getSession_id() { return session_id; }
    public void setSession_id(int session_id) { this.session_id = session_id; }
    public CourseSection getCourseSection() { return courseSection; }
    public void setCourseSection(CourseSection courseSection) { this.courseSection = courseSection; }
    public Date getSession_date() { return session_date; }
    public void setSession_date(Date session_date) { this.session_date = session_date; }
    public Time getStart_time() { return start_time; }
    public void setStart_time(Time start_time) { this.start_time = start_time; }
    public Time getEnd_time() { return end_time; }
    public void setEnd_time(Time end_time) { this.end_time = end_time; }
    public String getTopic_covered() { return topic_covered; }
    public void setTopic_covered(String topic_covered) { this.topic_covered = topic_covered; }
}