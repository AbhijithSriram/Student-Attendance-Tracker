package com.group1.dao;

import com.group1.model.AcademicPeriod;
import java.util.List;

public interface AcademicPeriodDao {
    void savePeriod(AcademicPeriod period);
    void updatePeriod(AcademicPeriod period);
    void deletePeriod(int periodId);
    AcademicPeriod getPeriodById(int periodId);
    List<AcademicPeriod> getAllPeriods();
    boolean isUsedInCourseSection(int periodId);
}