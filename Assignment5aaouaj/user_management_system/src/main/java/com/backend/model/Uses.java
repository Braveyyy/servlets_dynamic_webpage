package com.backend.model;

public class Uses {
    private int userId;
    private int deviceId;
    private String usageDate;
    private int usageDuration;

    // Constructor
    public Uses (int userId, int deviceId, String usageDate, int usageDuration) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.usageDate = usageDate;
        this.usageDuration = usageDuration;
    }

    // Getters and Setters
    public int getUID() {
        return userId;
    }

    public void setUID(int userId) {
        this.userId = userId;
    }

    public int getDID() {
        return deviceId;
    }
    
    public void setDID(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(String usageDate) {
        this.usageDate = usageDate;
    }

    public int getUsageDuration() {
        return usageDuration;
    }

    public void setUsageDuration(int usageDuration) {
        this.usageDuration = usageDuration;
    }

}