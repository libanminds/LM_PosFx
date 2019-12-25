package com.libanminds.repositories;

import com.libanminds.models.ItemCategory;
import com.libanminds.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ItemsCategoriesRepository {

    public static ObservableList<ItemCategory> getItemsCategories() {
        String query = "SELECT * FROM item_categories";

        return getItemsFromQuery(query);
    }

    public static ObservableList<ItemCategory> getItemsCategoriesLike(String value) {
        String query = "SELECT * FROM item_categories where name like '%" + value + "%'";

        return getItemsFromQuery(query);
    }

    public static boolean addItemCategory(ItemCategory category) {
        String query = "INSERT INTO item_categories(name) VALUES (?)";
        PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
        try {
            statement.setString(1, category.getName());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateItemCategory(ItemCategory category) {
        String query = "UPDATE item_categories SET name = ? WHERE id = ?";
        PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
        try {
            statement.setString(1, category.getName());
            statement.setInt(2, category.getID());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteItemCategory(ItemCategory category) {
        try {
            String query = "DELETE FROM item_categories where id = " + category.getID();
            Statement statement = DBConnection.instance.getStatement();
            statement.executeUpdate(query);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static ObservableList<ItemCategory> getItemsFromQuery(String query) {
        ObservableList<ItemCategory> data = FXCollections.observableArrayList();

        try {
            Statement statement = DBConnection.instance.getStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                data.add(new ItemCategory(
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
