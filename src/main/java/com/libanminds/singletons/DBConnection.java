package com.libanminds.singletons;

import com.libanminds.constants.Constants;

import java.sql.*;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection;

    public static DBConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Statement getStatement() {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PreparedStatement getPreparedStatement(String query) {
        try {
            return connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PreparedStatement getPreparedStatement(String query, int getGeneratedKeys) {
        try {
            return connection.prepareStatement(query, getGeneratedKeys);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private DBConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection(Constants.DB_CONNECTION_STRING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        return connection;
    }
}
