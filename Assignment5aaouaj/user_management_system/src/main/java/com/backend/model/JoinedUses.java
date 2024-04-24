package com.backend.model;

public class JoinedUses {
    private String userName;
    private String deviceName;
    private String usageDate;
    private int usageDuration;

    // Constructor
    public JoinedUses(String userName, String deviceName, String usageDate, int usageDuration) {
        this.userName = userName;
        this.deviceName = deviceName;
        this.usageDate = usageDate;
        this.usageDuration = usageDuration;
    }

    // Getters and Setters
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeviceName() {
        return deviceName;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
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