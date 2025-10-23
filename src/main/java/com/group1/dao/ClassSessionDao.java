package com.group1.dao;

import com.group1.model.ClassSession;
import java.util.List;

public interface ClassSessionDao {
    ClassSession saveSession(ClassSession session);
    void updateSession(ClassSession session);
    void deleteSession(int sessionId);
    ClassSession getSessionById(int sessionId);
    List<ClassSession> getSessionsBySection(int sectionId);
}