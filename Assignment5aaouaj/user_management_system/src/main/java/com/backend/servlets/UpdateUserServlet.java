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

@WebServlet("/updateUser")
public class UpdateUserServlet extends HttpServlet {

    // POST Request
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        // Variables
        StringBuilder requestBody = new StringBuilder();
        String line;
        String updateUserResult, jsonResult;
        PrintWriter out = null;
        Connection connection = null;
        PreparedStatement statement = null, nameDupeStatement = null, idExistsStatement = null;
        ResultSet rs1 = null, rs2 = null;

        // Reads request body
        try (BufferedReader reader = request.getReader()) {
            while((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        } catch (IOException e) {
            throw new ServletException(e);
        }

        // Convert request body JSON into User object to easily access elements
        Gson gson = new Gson();
        User user = gson.fromJson(requestBody.toString(), User.class);
        try {
            // Registers JBDC Driver
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        try {
            // Open JBDC connection
            connection = DriverManager.getConnection(JBDCinfo.getUrl(), JBDCinfo.getUsername(), JBDCinfo.getPassword());

            // Check to make sure ID exists.
            String checkIdSql = "SELECT COUNT(*) FROM Users WHERE UserID = ?";
            idExistsStatement = connection.prepareStatement(checkIdSql);
            idExistsStatement.setInt(1, user.getUserId());
            rs2 = idExistsStatement.executeQuery();
            if(rs2.next() && rs2.getInt(1) == 0) {
                updateUserResult = "Existing ID doesn't exist. Please enter an existing ID.";
                jsonResult = gson.toJson(updateUserResult);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out = response.getWriter();
                out.print(jsonResult);
                out.flush();
                return;
            }

            // Check to make sure new user name isn't already in use.
            String checkNameSql = "SELECT COUNT(*) FROM Users WHERE UserName = ?";
            nameDupeStatement = connection.prepareStatement(checkNameSql);
            nameDupeStatement.setString(1, user.getUserName());
            rs1 = nameDupeStatement.executeQuery();
            if(rs1.next() && rs1.getInt(1) > 0) {
                updateUserResult = "Username already in use, please select a different one.";
                jsonResult = gson.toJson(updateUserResult);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out = response.getWriter();
                out.print(jsonResult);
                out.flush();
                return;
            }

            // Both checks complete, SQL update statement.
            String updateSql = "UPDATE Users SET UserName = ?, UserType = ? WHERE UserID = ?";
            statement = connection.prepareStatement(updateSql);
            // SQL paramaters
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getUserType());
            statement.setInt(3, user.getUserId());

            // Run SQL statement, print results to console
            int result = statement.executeUpdate();
            System.out.println("(UPDATE USER) Update Result: " + result);

            // Send JSON response to client
            updateUserResult = "Sucessfully updated user in the database.";
            jsonResult = gson.toJson(updateUserResult);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.print(jsonResult);
            out.flush();
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
        finally {
            // Close resources
            try { if (statement != null) statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (nameDupeStatement != null) nameDupeStatement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (idExistsStatement != null) idExistsStatement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (rs1 != null) rs1.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (rs2 != null) rs2.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
} 