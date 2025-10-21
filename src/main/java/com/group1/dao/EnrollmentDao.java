package com.group1.dao;

import com.group1.model.Enrollment;
import java.util.List;

public interface EnrollmentDao {
    void saveEnrollment(Enrollment enrollment);
    void deleteEnrollment(int enrollmentId);
    Enrollment getEnrollmentById(int enrollmentId);
    List<Enrollment> getEnrollmentsBySection(int sectionId);
}