package com.group1.dao;

import com.group1.model.SectionSlotAllocation;
import com.group1.model.SectionSlotAllocation.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface SectionSlotAllocationDao {
    void saveAllocation(SectionSlotAllocation allocation);
    void updateAllocation(SectionSlotAllocation allocation);
    void deleteAllocation(int allocationId);
    SectionSlotAllocation getAllocationById(int allocationId);
    List<SectionSlotAllocation> getAllocationsBySection(int sectionId);
    List<SectionSlotAllocation> getAllocationsBySlotAndDay(int slotId, DayOfWeek dayOfWeek);
    Optional<SectionSlotAllocation> findBySectionSlotDay(int sectionId, int slotId, DayOfWeek dayOfWeek);

    // Check for conflicts - returns true if a conflict exists
    boolean hasConflict(int studentGroupId, int slotId, DayOfWeek dayOfWeek, int periodId, int excludeSectionId);
}
