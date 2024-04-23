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

import com.backend.model.Uses;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/searchUsage")
public class UsesSearchServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        List<Uses> searchUsageResult = new ArrayList<>();
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
            String sql = "";
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}