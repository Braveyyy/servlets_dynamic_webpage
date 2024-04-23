package com.backend.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.backend.model.User;
import com.google.gson.Gson;

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
        String addUserResult = "";
        String jsonResult = "";
        PrintWriter out = null;
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement dupeStatement = null;
        ResultSet rs = null;
        
        try (BufferedReader reader = request.getReader()) {
            while((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        } catch (IOException e) {
            throw new ServletException(e);
        }

        String jsonBody = requestBody.toString();
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
            
            // Check for Duplicate IDs/Names
            String checkSql = "SELECT COUNT(*) FROM Users WHERE UserID = ? OR UserName = ?";
            dupeStatement = connection.prepareStatement(checkSql);
            dupeStatement.setInt(1, user.getUserId());
            dupeStatement.setString(2, user.getUserName());
            rs = dupeStatement.executeQuery();
            if(rs.next() && rs.getInt(1) > 0) {
                addUserResult = "Duplicate UserID or UserName";
                jsonResult = gson.toJson(addUserResult);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out = response.getWriter();
                out.print(jsonResult);
                out.flush();
                return;
            }

            // SQL statement
            String insertSql = "INSERT INTO Users (UserID, UserName, UserType) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(insertSql);

            statement.setInt(1, user.getUserId());
            statement.setString(2, user.getUserName());
            statement.setString(3, user.getUserType());

            int result = statement.executeUpdate();
            System.out.println("Insertion Result: " + result);
            
            addUserResult = "Successfully added user to the database.";
            jsonResult = gson.toJson(addUserResult);
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
            try { if (dupeStatement != null) dupeStatement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}