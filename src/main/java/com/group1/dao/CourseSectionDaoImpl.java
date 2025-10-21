package com.group1.dao;

import com.group1.model.CourseSection;
import com.group1.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class CourseSectionDaoImpl implements CourseSectionDao {

    @Override
    public void saveSection(CourseSection section) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(section);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateSection(CourseSection section) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(section);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSection(int sectionId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CourseSection section = session.get(CourseSection.class, sectionId);
            if (section != null) {
                // To avoid foreign key errors, delete dependent records first
                // 1. Delete Attendance records linked to sessions of this section
                session.createMutationQuery(
                    "delete from Attendance where classSession.session_id in " +
                    "(select session_id from ClassSession where courseSection.section_id = :sectId)")
                    .setParameter("sectId", sectionId).executeUpdate();

                // 2. Delete Class Sessions for this section
                session.createMutationQuery("delete from ClassSession where courseSection.section_id = :sectId")
                    .setParameter("sectId", sectionId).executeUpdate();

                // 3. Delete Enrollments for this section
                session.createMutationQuery("delete from Enrollment where courseSection.section_id = :sectId")
                    .setParameter("sectId", sectionId).executeUpdate();

                // 4. Finally, delete the course section itself
                session.remove(section);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public CourseSection getSectionById(int sectionId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(CourseSection.class, sectionId);
        }
    }

    @Override
    public List<CourseSection> getAllSections() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from CourseSection", CourseSection.class).list();
        }
    }

    @Override
    public List<CourseSection> getSectionsByProfessor(String employeeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<CourseSection> query = session.createQuery(
                "FROM CourseSection cs WHERE cs.professor.employee_id = :profId",
                CourseSection.class
            );
            query.setParameter("profId", employeeId);
            return query.list();
        }
    }
}