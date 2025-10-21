package com.group1.dao;

import com.group1.model.Student;
import java.util.List;

public interface StudentDao {
    void saveStudent(Student student);
    void updateStudent(Student student);
    void deleteStudent(String regNumber);
    Student getStudentByRegNumber(String regNumber);
    List<Student> getAllStudents();
}