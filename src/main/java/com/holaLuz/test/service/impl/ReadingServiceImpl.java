package com.holaLuz.test.service.impl;

import com.holaLuz.test.entity.ReadingEntity;
import com.holaLuz.test.entity.SuspiciousReadingEntity;
import com.holaLuz.test.service.ReadingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class ReadingServiceImpl implements ReadingService {

    /**
     * Compares reading values to detect suspicious readings
     * @param data the readings to compare
     * @return a list of SuspiciousReadingEntity
     */
    @Override
    public List<SuspiciousReadingEntity> getSuspiciousReadings(Map<String,List<ReadingEntity>> data) {
        if(data.isEmpty()){
            return List.of();
        }
        List<SuspiciousReadingEntity> suspiciousReadingEntityList=new ArrayList<>();
        for(Map.Entry<String,List<ReadingEntity>> entry: data.entrySet()){
            List<ReadingEntity> readings=entry.getValue();
            BigDecimal median=BigDecimal.valueOf(readings.stream().map(ReadingEntity::getReading).
                    mapToInt(Integer::intValue).average().orElse(0.0));
            readings.forEach(reading ->{
                if(isSuspicious(reading,median)){
                    suspiciousReadingEntityList.add(buildSuspiciousReadingEntity(reading,median));
                }
            });
        }
        return suspiciousReadingEntityList;
    }

    /**
     * Map ReadingEntity to SuspiciousReadingEntity
     * @param reading the ReadingEntity
     * @param median the average of all readings
     * @return the SuspiciousReadingEntity instance
     */
    private SuspiciousReadingEntity buildSuspiciousReadingEntity(ReadingEntity reading,BigDecimal median){
        SuspiciousReadingEntity suspiciousReadingEntity=new SuspiciousReadingEntity();
        suspiciousReadingEntity.setClientId(reading.getClient());
        suspiciousReadingEntity.setMonth(reading.getPeriod());
        suspiciousReadingEntity.setSuspiciousValue(reading.getReading());
        suspiciousReadingEntity.setMedian(median.setScale(2,RoundingMode.HALF_UP));
        return suspiciousReadingEntity;
    }

    /**
     * check if the reading is suspicious
     * @param reading the ReadingEntity
     * @param median the average of all readings
     * @return boolean value
     */
    private boolean isSuspicious(ReadingEntity reading,BigDecimal median) {
        BigDecimal errorMargin=median.divide(BigDecimal.valueOf(2));
        BigDecimal maxValue=median.add(errorMargin);
        BigDecimal minValue=median.subtract(errorMargin);
        return reading.getReading()>maxValue.intValue() || reading.getReading()<minValue.intValue();
    }
}
