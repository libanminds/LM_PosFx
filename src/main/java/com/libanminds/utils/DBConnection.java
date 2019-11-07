package com.libanminds.utils;

import java.sql.*;


//TODO This file will only contain the connection instance, we should put database requests in other classes for readability. I added the test connection query here for testing purposes only.
public class DBConnection {
    //singleton, initialized in MainApp
    public static DBConnection instance;

    private Connection connection;

    public DBConnection () {
        connection = connect();
    }

    private Connection connect() {
        String connect_string = "jdbc:sqlite:pos.db";

        Connection connectionInstance = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connectionInstance = DriverManager.getConnection(connect_string);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connectionInstance;
    }

    public Statement getStatement() {
        try {
            return connection.createStatement();
        }catch (SQLException e) {
            return null;
        }
    }

    public PreparedStatement getPreparedStatement(String query) {
        try {
            return connection.prepareStatement(query);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
