package com.backend.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/deleteUser")
public class DeleteUserServlet extends HttpServlet {

    // DELETE request
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        // Variables
        int userId = Integer.parseInt(request.getParameter("userId"));
        String deleteUserResult, jsonResult;
        Connection connection = null;
        PreparedStatement statement = null, idExistsStatement = null;
        ResultSet rs1 = null;
        PrintWriter out = null;
        try {
            // Registers JBDC Driver
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Gson gson = new Gson();
        try {
            // Establish JBDC database connection
        	connection = DriverManager.getConnection(JBDCinfo.getUrl(), JBDCinfo.getUsername(), JBDCinfo.getPassword());

            // Check if ID exists
            String checkIdSql = "SELECT COUNT(*) FROM Users WHERE UserID = ?";
            idExistsStatement = connection.prepareStatement(checkIdSql);
            idExistsStatement.setInt(1, userId);
            rs1 = idExistsStatement.executeQuery();
            if(rs1.next() && rs1.getInt(1) == 0) {
                deleteUserResult = "Existing ID doesn't exist. Please enter an existing ID.";
                jsonResult = gson.toJson(deleteUserResult);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out = response.getWriter();
                out.print(jsonResult);
                out.flush();
                return;
            }

            // Deletion SQL Statement
            String deleteSql = "DELETE FROM Users WHERE UserID = ?";
            statement = connection.prepareStatement(deleteSql);
            statement.setInt(1, userId);
            
            // Execute SQL statement, print results to console
            int deletedUserRows = statement.executeUpdate();
            System.out.println("(DELETE USER) User Rows Deleted: " + deletedUserRows);

            // Send JSON response to client
            deleteUserResult = "Successfuly deleted user in the database.";
            jsonResult = gson.toJson(deleteUserResult);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.print(jsonResult);
            out.flush();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (idExistsStatement != null) idExistsStatement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (rs1 != null) rs1.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}