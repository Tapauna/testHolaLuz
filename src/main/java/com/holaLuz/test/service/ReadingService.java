package com.holaLuz.test.service;


import com.holaLuz.test.entity.ReadingEntity;
import com.holaLuz.test.entity.SuspiciousReadingEntity;

import java.util.List;
import java.util.Map;

public interface ReadingService {
    /**
     * Compares reading values to detect suspicious readings
     * @param data the readings to compare
     * @return a list of SuspiciousReadingEntity
     */
    public List<SuspiciousReadingEntity> getSuspiciousReadings(Map<String,List<ReadingEntity>> data);
}
