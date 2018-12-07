package com.database.demo.service;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class SqlServerUtil {
    private static final String USERNAME = "root";
    private static final String PASSWORD = "101010";
    private static final String M_CONN_STRING =
            "jdbc:sqlserver://localhost:1433;databaseName=data";

    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(M_CONN_STRING, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String processException(SQLException e) {
        System.err.println("Error message: " + e.getMessage());
        System.err.println("Error code: " + e.getErrorCode());
        System.err.println("SQL state: " + e.getSQLState());
        return ("Error message: " + e.getMessage() + " " + "Error code: " + e.getErrorCode() + " " + "SQL state: " + e.getSQLState());
    }

}
