package com.group1.dao;

import com.group1.model.Slot;
import java.util.List;
import java.util.Optional;

public interface SlotDao {
    void saveSlot(Slot slot);
    void updateSlot(Slot slot);
    void deleteSlot(int slotId);
    Slot getSlotById(int slotId);
    Optional<Slot> getSlotByNumber(int slotNumber);
    List<Slot> getAllSlots();
}
