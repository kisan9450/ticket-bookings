package com.xalts.authservice;

import java.sql.*;

public class JdbcTest {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/booking_platform?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useLegacyDatetimeCode=false";
        String user = "kk";
        String password = "Kisan@9450";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }
}