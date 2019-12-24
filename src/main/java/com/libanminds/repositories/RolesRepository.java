package com.libanminds.repositories;

import com.libanminds.models.Role;
import com.libanminds.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RolesRepository {

    public static ObservableList<Role> getRoles() {
        String query = "SELECT * FROM role";

        return getRolesFromQuery(query);
    }

//    public static ObservableList<ItemCategory> getItemsCategoriesLike(String value) {
//        String query = "SELECT * FROM item_categories where name like '%"+ value + "%'";
//
//        return getRolesFromQuery(query);
//    }

//    public static boolean addItemCategory(ItemCategory category) {
//        String query = "INSERT INTO item_categories(name) VALUES (?)";
//        PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
//        try {
//            statement.setString(1, category.getName());
//            statement.executeUpdate();
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public static boolean updateItemCategory(ItemCategory category) {
//        String query = "UPDATE item_categories SET name = ? WHERE id = ?";
//        PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
//        try {
//            statement.setString(1, category.getName());
//            statement.setInt(2, category.getID());
//            statement.executeUpdate();
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public static boolean deleteItemCategory(ItemCategory category) {
//        try {
//            String query = "DELETE FROM item_categories where id = " + category.getID();
//            Statement statement  = DBConnection.instance.getStatement();
//            statement.executeUpdate(query);
//            return true;
//        }catch (Exception e) {
//            System.out.println(e.getMessage());
//            return false;
//        }
//    }

    private static ObservableList<Role> getRolesFromQuery(String query) {
        ObservableList<Role> data = FXCollections.observableArrayList();

        try {
            Statement statement  = DBConnection.instance.getStatement();
            ResultSet rs    = statement.executeQuery(query);
            while (rs.next()) {
                data.add(new Role(
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
