package com.libanminds.repositories;

import com.libanminds.models.Item;
import com.libanminds.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ItemsRepository {
    public static ObservableList<Item> getItems() {
        String query = "SELECT * FROM items";
        ObservableList<Item> data = FXCollections.observableArrayList();

        try {
            Statement statement  = DBConnection.instance.getStatement();
            ResultSet rs    = statement.executeQuery(query);
            while (rs.next()) {
                data.add(new Item(
                        rs.getString("image"),
                        rs.getInt("code"),
                        rs.getString("name"),
                        "Category",
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        "Jack Sparrow",
                        //rs.getString("supplier"),
                        rs.getString("description"),
                        90,
                        false,
                        //rs.getBoolean("isService"),
                        rs.getString("created_at")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return data;
    }
}
