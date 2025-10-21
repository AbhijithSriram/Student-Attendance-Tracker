package com.group1.dao;

import com.group1.model.AcademicPeriod;
import com.group1.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class AcademicPeriodDaoImpl implements AcademicPeriodDao {

    @Override
    public void savePeriod(AcademicPeriod period) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(period);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePeriod(AcademicPeriod period) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(period);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePeriod(int periodId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            AcademicPeriod period = session.get(AcademicPeriod.class, periodId);
            if (period != null) {
                session.remove(period);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public AcademicPeriod getPeriodById(int periodId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(AcademicPeriod.class, periodId);
        }
    }

    @Override
    public List<AcademicPeriod> getAllPeriods() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from AcademicPeriod", AcademicPeriod.class).list();
        }
    }
    @Override
    public boolean isUsedInCourseSection(int periodId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "select count(*) from CourseSection where academicPeriod.period_id = :pid", Long.class);
            query.setParameter("pid", periodId);
            return query.getSingleResult() > 0;
        }
    }
}