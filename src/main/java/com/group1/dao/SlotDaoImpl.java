package com.group1.dao;

import com.group1.model.Slot;
import com.group1.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Optional;

public class SlotDaoImpl implements SlotDao {

    @Override
    public void saveSlot(Slot slot) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(slot);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateSlot(Slot slot) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(slot);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSlot(int slotId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Slot slot = session.get(Slot.class, slotId);
            if (slot != null) {
                session.remove(slot);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Slot getSlotById(int slotId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Slot.class, slotId);
        }
    }

    @Override
    public Optional<Slot> getSlotByNumber(int slotNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Slot> query = session.createQuery(
                "FROM Slot WHERE slot_number = :slotNumber",
                Slot.class
            );
            query.setParameter("slotNumber", slotNumber);
            return query.uniqueResultOptional();
        }
    }

    @Override
    public List<Slot> getAllSlots() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Slot ORDER BY slot_number", Slot.class).list();
        }
    }
}
