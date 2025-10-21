package com.group1.dao;

import com.group1.model.ClassSession;
import java.util.List;

public interface ClassSessionDao {
    ClassSession saveSession(ClassSession session); // Returns the saved session with its ID
    void updateSession(ClassSession session); // New method
    void deleteSession(int sessionId);
    ClassSession getSessionById(int sessionId);
    List<ClassSession> getSessionsBySection(int sectionId);
}