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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        StringBuilder requestBody = new StringBuilder();
        String line;
        String updateUserResult, jsonResult;
        PrintWriter out = null;
        Connection connection = null;
        PreparedStatement statement = null;

        try (BufferedReader reader = request.getReader()) {
            while((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        } catch (IOException e) {
            throw new ServletException(e);
        }

        Gson gson = new Gson();
        User user = gson.fromJson(requestBody.toString(), User.class);
        try {
            // Registers Driver
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        try {
            connection = DriverManager.getConnection(JBDCinfo.getUrl(), JBDCinfo.getUsername(), JBDCinfo.getPassword());

            // Check to make sure new user name isn't already in use.
            // Check to make sure ID exists.

        } catch(SQLException e) {
            e.printStackTrace();
        }
        finally {
            // Close resources
        }
    }
} 