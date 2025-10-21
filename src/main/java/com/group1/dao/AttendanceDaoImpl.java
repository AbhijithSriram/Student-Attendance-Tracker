package com.group1.dao;

import com.group1.model.Attendance;
import com.group1.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Optional;

public class AttendanceDaoImpl implements AttendanceDao {

    @Override
    public void saveAttendance(Attendance attendance) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(attendance);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateAttendance(Attendance attendance) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(attendance);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Attendance getAttendanceById(int attendanceId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Attendance.class, attendanceId);
        }
    }

    @Override
    public List<Attendance> getAttendanceBySession(int sessionId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Attendance> query = session.createQuery(
                "FROM Attendance a WHERE a.classSession.session_id = :sessionId", 
                Attendance.class
            );
            query.setParameter("sessionId", sessionId);
            return query.list();
        }
    }

    @Override
    public List<Attendance> getAttendanceByStudent(String regNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Attendance> query = session.createQuery(
                "FROM Attendance a WHERE a.student.reg_number = :regNumber", 
                Attendance.class
            );
            query.setParameter("regNumber", regNumber);
            return query.list();
        }
    }

    @Override
    public Optional<Attendance> getAttendanceForStudentInSession(int sessionId, String regNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Attendance> query = session.createQuery(
                "FROM Attendance a WHERE a.classSession.session_id = :sessionId AND a.student.reg_number = :regNumber",
                Attendance.class
            );
            query.setParameter("sessionId", sessionId);
            query.setParameter("regNumber", regNumber);
            return query.uniqueResultOptional();
        }
    }
}