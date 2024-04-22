package com.backend.servlets;

import com.backend.servlets.JBDCinfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.backend.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/addUser")
public class AddUserServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        StringBuilder requestBody = new StringBuilder();
        String line;
        Connection connection = null;
        PreparedStatement statement = null;
        try (BufferedReader reader = request.getReader()) {
            while((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        } catch (IOException e) {
            throw new ServletException(e);
        }

        String jsonBody = requestBody.toString();
        System.out.println("*********JSONBODY STRING: *************" + jsonBody);
        Gson gson = new Gson();
        User user = gson.fromJson(jsonBody, User.class);
        try {
            // Registers Driver
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        try {
        	connection = DriverManager.getConnection(JBDCinfo.getUrl(), JBDCinfo.getUsername(), JBDCinfo.getPassword());
            
            // SQL statement
            String sql = "INSERT INTO Users (UserID, UserName, UserType) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(sql);

            System.out.println("********RECEIVED ID BEFORE ADDING TO SQL STATEMENT:********* " +user.getUserId());
            System.out.println("********RECEIVED USERNAME BEFORE ADDING TO SQL STATEMENT:********* " +user.getUserName());
            System.out.println("********RECEIVED TYPE BEFORE ADDING TO SQL STATEMENT:********* " +user.getUserType());

            statement.setString(1, Integer.toString(user.getUserId()));
            statement.setString(2, user.getUserName());
            statement.setString(3, user.getUserType());

            int result = statement.executeUpdate();
            System.out.println("Insertion Result: " + result);

        
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try { if (statement != null) statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        try{
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\": true}");
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}