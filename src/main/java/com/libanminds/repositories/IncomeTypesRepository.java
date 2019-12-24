package com.libanminds.repositories;

import com.libanminds.models.Type;
import com.libanminds.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class IncomeTypesRepository {

    public static ObservableList<Type> getIncomeTypes() {
        String query = "SELECT * FROM income_types";

        return getTypesFromQuery(query);
    }

    public static ObservableList<Type> getIncomeTypesLike(String value) {
        String query = "SELECT * FROM income_types where name like '%"+ value + "%'";

        return getTypesFromQuery(query);
    }

    public static boolean addIncomeType(Type type) {
        String query = "INSERT INTO income_types(name) VALUES (?)";
        PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
        try {
            statement.setString(1, type.getName());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateIncomeType(Type type) {
        String query = "UPDATE income_types SET name = ? WHERE id = ?";
        PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
        try {
            statement.setString(1, type.getName());
            statement.setInt(2, type.getID());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteIncomeType(Type type) {
        try {
            String query = "DELETE FROM income_types where id = " + type.getID();
            Statement statement  = DBConnection.instance.getStatement();
            statement.executeUpdate(query);
            return true;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static ObservableList<Type> getTypesFromQuery(String query) {
        ObservableList<Type> data = FXCollections.observableArrayList();

        try {
            Statement statement  = DBConnection.instance.getStatement();
            ResultSet rs    = statement.executeQuery(query);
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
