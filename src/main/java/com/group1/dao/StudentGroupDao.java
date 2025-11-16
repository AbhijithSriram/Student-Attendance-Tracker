package com.group1.dao;

import com.group1.model.StudentGroup;
import java.util.List;
import java.util.Optional;

public interface StudentGroupDao {
    void saveStudentGroup(StudentGroup studentGroup);
    void updateStudentGroup(StudentGroup studentGroup);
    void deleteStudentGroup(int groupId);
    StudentGroup getStudentGroupById(int groupId);
    Optional<StudentGroup> getStudentGroupByCode(String groupCode);
    List<StudentGroup> getAllStudentGroups();
    Optional<StudentGroup> findByDepartmentYearProgram(
        StudentGroup.Department department,
        int startingYear,
        StudentGroup.ProgramType programType
    );
}
