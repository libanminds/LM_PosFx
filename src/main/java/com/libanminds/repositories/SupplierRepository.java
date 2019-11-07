package com.libanminds.repositories;

import com.libanminds.models.Supplier;
import com.libanminds.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SupplierRepository {
    public static ObservableList<Supplier> getSuppliers() {

        String query = "SELECT * FROM suppliers";
        ObservableList<Supplier> data = FXCollections.observableArrayList();
        try {
            Statement statement  = DBConnection.instance.getStatement();
            ResultSet rs    = statement.executeQuery(query);
            while (rs.next()) {
                data.add(new Supplier(
                        rs.getString("first_name") + " " + rs.getString("last_name"),
                        rs.getString("company"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        0
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return data;
    }
}
