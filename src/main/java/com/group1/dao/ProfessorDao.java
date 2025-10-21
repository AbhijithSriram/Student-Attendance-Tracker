package com.group1.dao;

import com.group1.model.Professor;
import java.util.List;

public interface ProfessorDao {
    void saveProfessor(Professor professor);
    void updateProfessor(Professor professor);
    void deleteProfessor(String employeeId);
    Professor getProfessorById(String employeeId);
    List<Professor> getAllProfessors();
}