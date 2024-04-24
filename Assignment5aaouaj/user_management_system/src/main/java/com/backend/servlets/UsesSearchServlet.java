package com.backend.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
 
import com.backend.model.JoinedUses;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/searchUsage")
public class UsesSearchServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        List<JoinedUses> searchUsageResult = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Registers Driver
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        try {
            // Establish database connection
            connection = DriverManager.getConnection(JBDCinfo.getUrl(), JBDCinfo.getUsername(), JBDCinfo.getPassword());

            // SQL statement
            String sql = "SELECT u.UserName, d.DeviceName, us.UsageDate, us.UsageDuration FROM uses us JOIN users u ON us.UserID = u.UserID JOIN devices d ON us.DeviceID = d.DeviceID WHERE us.UserID = ? AND us.UsageDate BETWEEN ? AND ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setString(2, startDate);
            statement.setString(3, endDate);

            // Execute query
            resultSet = statement.executeQuery();

            // Process result set
            while(resultSet.next()) {
                String userName = resultSet.getString("UserName");
                String deviceName = resultSet.getString("DeviceName");
                String usageDate = resultSet.getString("UsageDate");
                int usageDuration = resultSet.getInt("UsageDuration");
                System.out.println("(JOIN SEARCH) Username: " + userName + "\n Device Name:  "
                    + deviceName + "\n Usage Date: " + usageDate + "\n Usage Duration: " + usageDuration);
                // Create JoinedUses object and add to search result
                JoinedUses uses = new JoinedUses(userName, deviceName, usageDate, usageDuration);
                searchUsageResult.add(uses);
            }


        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try { if (resultSet != null) resultSet.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        // Convert searchResult to JSON and send as response
        Gson gson = new Gson();
        String jsonResult = gson.toJson(searchUsageResult);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsonResult);
        out.flush();
    }
}