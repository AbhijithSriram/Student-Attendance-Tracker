package com.group1.dao;

import com.group1.model.Enrollment;
import com.group1.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class EnrollmentDaoImpl implements EnrollmentDao {

    @Override
    public void saveEnrollment(Enrollment enrollment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(enrollment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteEnrollment(int enrollmentId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Enrollment enrollment = session.get(Enrollment.class, enrollmentId);
            if (enrollment != null) {
                session.remove(enrollment);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Enrollment getEnrollmentById(int enrollmentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Enrollment.class, enrollmentId);
        }
    }

    @Override
    public List<Enrollment> getEnrollmentsBySection(int sectionId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Enrollment> query = session.createQuery(
                "FROM Enrollment e WHERE e.courseSection.section_id = :sectionId", 
                Enrollment.class
            );
            query.setParameter("sectionId", sectionId);
            return query.list();
        }
    }
}