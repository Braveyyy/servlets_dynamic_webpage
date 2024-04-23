package com.backend.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.backend.model.Uses;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/addUsage")
public class AddUsageServlet extends HttpServlet {

    // POST request
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        // Variables
        StringBuilder requestBody = new StringBuilder();
        String line;
        String addUsageResult, jsonResult;
        PrintWriter out = null;
        Connection connection = null;
        PreparedStatement statement = null;

        // Reads request body
        try (BufferedReader reader = request.getReader()) {
            while((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        } catch (IOException e) {
            throw new ServletException(e);
        }

        // Convert request body JSON into Uses object to easily access elements
        Gson gson = new Gson();
        Uses uses = gson.fromJson(requestBody.toString(), Uses.class);
        try {
            // Registers JBDC Driver
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        try {
            // Open JBDC connection
        	connection = DriverManager.getConnection(JBDCinfo.getUrl(), JBDCinfo.getUsername(), JBDCinfo.getPassword());
            
            // SQL statement
            String usageSql = "INSERT INTO Uses (UserID, DeviceID, UsageDate, UsageDuration) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(usageSql);

            statement.setInt(1, uses.getUID());
            statement.setInt(2, uses.getDID());
            statement.setString(3, uses.getUsageDate());
            statement.setInt(4, uses.getUsageDuration());

            // Run SQL statement, print results to console
            int result = statement.executeUpdate();
            System.out.println("(ADD USAGE) Insertion Result: " + result);

            // Send JSON response to client
            addUsageResult = "Successfully added usage record to the database.";
            jsonResult = gson.toJson(addUsageResult);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.print(jsonResult);
            out.flush();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try { if (statement != null) statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

    }
}