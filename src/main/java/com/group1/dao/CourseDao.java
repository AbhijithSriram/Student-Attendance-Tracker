package com.group1.dao;

import com.group1.model.Course;
import java.util.List;

public interface CourseDao {
    void saveCourse(Course course);
    void updateCourse(Course course);
    void deleteCourse(String courseCode);
    Course getCourseByCode(String courseCode);
    List<Course> getAllCourses();
    boolean hasAttendanceRecords(String courseCode);
}