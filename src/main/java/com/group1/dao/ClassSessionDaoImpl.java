package com.group1.dao;

import com.group1.model.ClassSession;
import com.group1.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

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
}