package com.group1.dao;

import com.group1.model.ClassSession;
import com.group1.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class ClassSessionDaoImpl implements ClassSessionDao {

    @Override
    public ClassSession saveSession(ClassSession classSession) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(classSession);
            transaction.commit();
            return classSession;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateSession(ClassSession classSession) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(classSession);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSession(int sessionId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            ClassSession classSession = session.get(ClassSession.class, sessionId);
            if (classSession != null) {
                session.remove(classSession);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public ClassSession getSessionById(int sessionId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(ClassSession.class, sessionId);
        }
    }

    @Override
    public List<ClassSession> getSessionsBySection(int sectionId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ClassSession> query = session.createQuery(
                "FROM ClassSession cs WHERE cs.courseSection.section_id = :sectionId ORDER BY cs.session_date DESC",
                ClassSession.class
            );
            query.setParameter("sectionId", sectionId);
            return query.list();
        }
    }

    @Override
    public List<ClassSession> getSessionsByStudentGroup(int groupId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ClassSession> query = session.createQuery(
                "FROM ClassSession cs WHERE cs.studentGroup.group_id = :groupId ORDER BY cs.session_date DESC, cs.slot.slot_number",
                ClassSession.class
            );
            query.setParameter("groupId", groupId);
            return query.list();
        }
    }

    @Override
    public List<ClassSession> getSessionsBySlotAndDate(int slotId, Date sessionDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ClassSession> query = session.createQuery(
                "FROM ClassSession cs WHERE cs.slot.slot_id = :slotId AND cs.session_date = :sessionDate",
                ClassSession.class
            );
            query.setParameter("slotId", slotId);
            query.setParameter("sessionDate", sessionDate);
            return query.list();
        }
    }

    @Override
    public Optional<ClassSession> findByGroupSlotDate(int groupId, int slotId, Date sessionDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ClassSession> query = session.createQuery(
                "FROM ClassSession cs WHERE cs.studentGroup.group_id = :groupId " +
                "AND cs.slot.slot_id = :slotId AND cs.session_date = :sessionDate",
                ClassSession.class
            );
            query.setParameter("groupId", groupId);
            query.setParameter("slotId", slotId);
            query.setParameter("sessionDate", sessionDate);
            return query.uniqueResultOptional();
        }
    }

    @Override
    public boolean hasSessionConflict(int groupId, int slotId, Date sessionDate, int excludeSessionId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(cs) FROM ClassSession cs " +
                "WHERE cs.studentGroup.group_id = :groupId " +
                "AND cs.slot.slot_id = :slotId " +
                "AND cs.session_date = :sessionDate " +
                "AND cs.session_id != :excludeSessionId",
                Long.class
            );
            query.setParameter("groupId", groupId);
            query.setParameter("slotId", slotId);
            query.setParameter("sessionDate", sessionDate);
            query.setParameter("excludeSessionId", excludeSessionId);
            return query.uniqueResult() > 0;
        }
    }
}