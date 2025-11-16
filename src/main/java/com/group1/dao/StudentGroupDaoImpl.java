package com.group1.dao;

import com.group1.model.StudentGroup;
import com.group1.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Optional;

public class StudentGroupDaoImpl implements StudentGroupDao {

    @Override
    public void saveStudentGroup(StudentGroup studentGroup) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(studentGroup);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStudentGroup(StudentGroup studentGroup) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(studentGroup);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteStudentGroup(int groupId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            StudentGroup studentGroup = session.get(StudentGroup.class, groupId);
            if (studentGroup != null) {
                session.remove(studentGroup);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public StudentGroup getStudentGroupById(int groupId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(StudentGroup.class, groupId);
        }
    }

    @Override
    public Optional<StudentGroup> getStudentGroupByCode(String groupCode) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<StudentGroup> query = session.createQuery(
                "FROM StudentGroup WHERE group_code = :groupCode",
                StudentGroup.class
            );
            query.setParameter("groupCode", groupCode);
            return query.uniqueResultOptional();
        }
    }

    @Override
    public List<StudentGroup> getAllStudentGroups() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM StudentGroup ORDER BY group_code", StudentGroup.class).list();
        }
    }

    @Override
    public Optional<StudentGroup> findByDepartmentYearProgram(
            StudentGroup.Department department,
            int startingYear,
            StudentGroup.ProgramType programType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<StudentGroup> query = session.createQuery(
                "FROM StudentGroup WHERE department = :dept AND starting_year = :year AND program_type = :prog",
                StudentGroup.class
            );
            query.setParameter("dept", department);
            query.setParameter("year", startingYear);
            query.setParameter("prog", programType);
            return query.uniqueResultOptional();
        }
    }
}
