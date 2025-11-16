package com.group1.dao;

import com.group1.model.SectionSlotAllocation;
import com.group1.model.SectionSlotAllocation.DayOfWeek;
import com.group1.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Optional;

public class SectionSlotAllocationDaoImpl implements SectionSlotAllocationDao {

    @Override
    public void saveAllocation(SectionSlotAllocation allocation) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(allocation);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateAllocation(SectionSlotAllocation allocation) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(allocation);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllocation(int allocationId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            SectionSlotAllocation allocation = session.get(SectionSlotAllocation.class, allocationId);
            if (allocation != null) {
                session.remove(allocation);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public SectionSlotAllocation getAllocationById(int allocationId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(SectionSlotAllocation.class, allocationId);
        }
    }

    @Override
    public List<SectionSlotAllocation> getAllocationsBySection(int sectionId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<SectionSlotAllocation> query = session.createQuery(
                "FROM SectionSlotAllocation WHERE courseSection.section_id = :sectionId ORDER BY day_of_week, slot.slot_number",
                SectionSlotAllocation.class
            );
            query.setParameter("sectionId", sectionId);
            return query.list();
        }
    }

    @Override
    public List<SectionSlotAllocation> getAllocationsBySlotAndDay(int slotId, DayOfWeek dayOfWeek) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<SectionSlotAllocation> query = session.createQuery(
                "FROM SectionSlotAllocation WHERE slot.slot_id = :slotId AND day_of_week = :dayOfWeek",
                SectionSlotAllocation.class
            );
            query.setParameter("slotId", slotId);
            query.setParameter("dayOfWeek", dayOfWeek);
            return query.list();
        }
    }

    @Override
    public Optional<SectionSlotAllocation> findBySectionSlotDay(int sectionId, int slotId, DayOfWeek dayOfWeek) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<SectionSlotAllocation> query = session.createQuery(
                "FROM SectionSlotAllocation WHERE courseSection.section_id = :sectionId " +
                "AND slot.slot_id = :slotId AND day_of_week = :dayOfWeek",
                SectionSlotAllocation.class
            );
            query.setParameter("sectionId", sectionId);
            query.setParameter("slotId", slotId);
            query.setParameter("dayOfWeek", dayOfWeek);
            return query.uniqueResultOptional();
        }
    }

    @Override
    public boolean hasConflict(int studentGroupId, int slotId, DayOfWeek dayOfWeek, int periodId, int excludeSectionId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(ssa) FROM SectionSlotAllocation ssa " +
                "WHERE ssa.courseSection.studentGroup.group_id = :groupId " +
                "AND ssa.slot.slot_id = :slotId " +
                "AND ssa.day_of_week = :dayOfWeek " +
                "AND ssa.courseSection.academicPeriod.period_id = :periodId " +
                "AND ssa.courseSection.section_id != :excludeSectionId",
                Long.class
            );
            query.setParameter("groupId", studentGroupId);
            query.setParameter("slotId", slotId);
            query.setParameter("dayOfWeek", dayOfWeek);
            query.setParameter("periodId", periodId);
            query.setParameter("excludeSectionId", excludeSectionId);
            return query.uniqueResult() > 0;
        }
    }
}
