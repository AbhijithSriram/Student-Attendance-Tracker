package com.group1.dao;

import com.group1.model.CourseSection;
import java.util.List;

public interface CourseSectionDao {
    void saveSection(CourseSection section);
    void updateSection(CourseSection section);
    void deleteSection(int sectionId);
    CourseSection getSectionById(int sectionId);
    List<CourseSection> getAllSections();
    List<CourseSection> getSectionsByProfessor(String employeeId);
}