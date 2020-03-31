package com.libanminds.repositories;

import com.libanminds.models.Type;
import com.libanminds.singletons.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExpenseTypesRepository {

    public static ObservableList<Type> getExpenseTypes() {
        String query = "SELECT * FROM expense_types";

        return getTypesFromQuery(query);
    }

    public static ObservableList<Type> getExpenseTypesLike(String value) {
        String query = "SELECT * FROM expense_types where name like '%" + value + "%'";

        return getTypesFromQuery(query);
    }

    public static boolean addExpenseType(Type type) {
        String query = "INSERT INTO expense_types(name) VALUES (?)";
        try {
            PreparedStatement statement = DBConnection.getInstance().getPreparedStatement(query);
            statement.setString(1, type.getName());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateExpenseType(Type type) {
        String query = "UPDATE expense_types SET name = ? WHERE id = ?";
        try {
            PreparedStatement statement = DBConnection.getInstance().getPreparedStatement(query);
            statement.setString(1, type.getName());
            statement.setInt(2, type.getID());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteExpenseType(Type type) {
        try {
            String query = "DELETE FROM expense_types where id = " + type.getID();
            Statement statement = DBConnection.getInstance().getStatement();
            statement.executeUpdate(query);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static ObservableList<Type> getTypesFromQuery(String query) {
        ObservableList<Type> data = FXCollections.observableArrayList();

        try {
            Statement statement = DBConnection.getInstance().getStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                data.add(new Type(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return data;
    }
}
