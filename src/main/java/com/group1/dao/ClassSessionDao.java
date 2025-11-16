package com.group1.dao;

import com.group1.model.ClassSession;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface ClassSessionDao {
    ClassSession saveSession(ClassSession session);
    void updateSession(ClassSession session);
    void deleteSession(int sessionId);
    ClassSession getSessionById(int sessionId);
    List<ClassSession> getSessionsBySection(int sectionId);
    List<ClassSession> getSessionsByStudentGroup(int groupId);
    List<ClassSession> getSessionsBySlotAndDate(int slotId, Date sessionDate);
    Optional<ClassSession> findByGroupSlotDate(int groupId, int slotId, Date sessionDate);

    // Check for conflicts - returns true if a conflict exists
    boolean hasSessionConflict(int groupId, int slotId, Date sessionDate, int excludeSessionId);
}