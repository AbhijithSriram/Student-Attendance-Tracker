package com.group1.util;

import com.group1.dao.SlotDao;
import com.group1.dao.SlotDaoImpl;
import com.group1.model.Slot;
import java.sql.Time;
import java.util.List;

/**
 * Utility class to initialize the 8 fixed time slots in the database.
 * This should be run once when the system is first set up.
 */
public class SlotInitializer {

    private static final SlotDao slotDao = new SlotDaoImpl();

    public static void initializeSlots() {
        // Check if slots already exist
        List<Slot> existingSlots = slotDao.getAllSlots();
        if (!existingSlots.isEmpty()) {
            System.out.println("Slots already initialized. Found " + existingSlots.size() + " slots.");
            return;
        }

        System.out.println("Initializing 8 time slots...");

        // Define the 8 fixed time slots
        createSlot(1, "08:00:00", "08:45:00");
        createSlot(2, "08:45:00", "09:30:00");
        createSlot(3, "09:50:00", "10:35:00");
        createSlot(4, "10:35:00", "11:20:00");
        createSlot(5, "11:20:00", "12:05:00");
        createSlot(6, "13:05:00", "13:50:00");
        createSlot(7, "13:50:00", "14:35:00");
        createSlot(8, "14:55:00", "15:40:00");

        System.out.println("Successfully initialized 8 time slots.");
    }

    private static void createSlot(int slotNumber, String startTime, String endTime) {
        Slot slot = new Slot();
        slot.setSlot_number(slotNumber);
        slot.setStart_time(Time.valueOf(startTime));
        slot.setEnd_time(Time.valueOf(endTime));
        slotDao.saveSlot(slot);
        System.out.println("Created: " + slot);
    }

    // Main method for standalone execution
    public static void main(String[] args) {
        try {
            initializeSlots();
        } catch (Exception e) {
            System.err.println("Error initializing slots: " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
