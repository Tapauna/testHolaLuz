package com.holaLuz.test.entity;

import java.math.BigDecimal;

public class SuspiciousReadingEntity {
    String clientId;
    String month;
    Integer suspiciousValue;
    BigDecimal median;


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getSuspiciousValue() {
        return suspiciousValue;
    }

    public void setSuspiciousValue(Integer suspiciousValue) {
        this.suspiciousValue = suspiciousValue;
    }

    public BigDecimal getMedian() {
        return median;
    }

    public void setMedian(BigDecimal median) {
        this.median = median;
    }
}
