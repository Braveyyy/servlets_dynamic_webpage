package com.backend.servlets;

public enum JBDCinfo {
    INSTANCE;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/AAOUAJASSIGN4";
    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD = "";

    public static String getUrl() {
        return JDBC_URL;
    }

    public static String getUsername() {
        return JDBC_USERNAME;
    }

    public static String getPassword() {
        return JDBC_PASSWORD;
    }
}