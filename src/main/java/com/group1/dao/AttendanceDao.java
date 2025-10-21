package com.group1.dao;

import com.group1.model.Attendance;
import java.util.List;
import java.util.Optional;

public interface AttendanceDao {
    void saveAttendance(Attendance attendance);
    void updateAttendance(Attendance attendance);
    Attendance getAttendanceById(int attendanceId);
    List<Attendance> getAttendanceBySession(int sessionId);
    List<Attendance> getAttendanceByStudent(String regNumber);
    Optional<Attendance> getAttendanceForStudentInSession(int sessionId, String regNumber); // New method
}