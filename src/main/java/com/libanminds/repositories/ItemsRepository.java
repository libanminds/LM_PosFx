package com.libanminds.repositories;

import com.libanminds.models.Income;
import com.libanminds.models.Item;
import com.libanminds.models.ItemCategory;
import com.libanminds.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ItemsRepository {
    public static ObservableList<Item> getItems() {
        String query = "SELECT * FROM items LEFT JOIN item_categories ON items.category_id = item_categories.id";

        return getItemsFromQuery(query);
    }

    public static ObservableList<Item> getItemsLike(String value) {
        String query = "SELECT * FROM items LEFT JOIN item_categories ON items.category_id = item_categories.id where" +
                " code like '%" + value + "%' or" +
                " items.name like '%" + value + "%' or" +
                " item_categories.name like '%" + value + "%' or" +
                " description like '%"+ value + "%'";

        return getItemsFromQuery(query);
    }

    public static boolean addItem(Item item) {
        String query = "INSERT INTO items(code,image,name,category_id,cost,price,currency,quantity,description,min_stock,ttc) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
        try {
            statement.setInt(1, item.getCode()); // code
            statement.setString(2, item.getImageUrl()); // imagePath
            statement.setString(3, item.getName()); // name
            statement.setInt(4, item.getItemCategory().getID()); // category id
            statement.setDouble(5, item.getCost()); // cost
            statement.setDouble(6, item.getPrice()); // price
            statement.setString(7, item.getCurrency()); //currency
            statement.setInt(8, item.getStock()); //quantity
            statement.setString(9, item.getDescription()); //description
            statement.setInt(10, item.getMinStock()); // min stock
            statement.setBoolean(11, item.getPriceIncludesTax()); // price includes tax
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateItem(Item item) {
        String query = "UPDATE items SET code = ? , image = ? , name = ?, category_id = ?, cost = ? , price = ?, currency = ? , quantity = ?, description = ?, min_stock = ?, ttc = ? where id = ?";

        PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
        try {
            statement.setInt(1, item.getCode()); // code
            statement.setString(2, item.getImageUrl()); // imagePath
            statement.setString(3, item.getName()); // name
            statement.setInt(4, item.getItemCategory().getID()); // category id
            statement.setDouble(5, item.getCost()); // cost
            statement.setDouble(6, item.getPrice()); // price
            statement.setString(7, item.getCurrency()); //currency
            statement.setInt(8, item.getStock()); //quantity
            statement.setString(9, item.getDescription()); //description
            statement.setInt(10, item.getMinStock()); // min stock
            statement.setBoolean(11, item.getPriceIncludesTax()); // price includes tax
            statement.setInt(12, item.getID()); // price i
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteItem(Item item) {
        try {
            String query = "DELETE FROM items where id = " + item.getID();
            Statement statement  = DBConnection.instance.getStatement();
            statement.executeUpdate(query);
            return true;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //NOTE: Column indexes are used when the result have duplicate column names.
    private static ObservableList<Item> getItemsFromQuery(String query) {
        ObservableList<Item> data = FXCollections.observableArrayList();
        try {
            Statement statement  = DBConnection.instance.getStatement();
            ResultSet rs    = statement.executeQuery(query);
            while (rs.next()) {
                data.add(new Item(
                        rs.getInt(1),
                        rs.getString("image"),
                        rs.getInt("code"),
                        rs.getString(4),
                        new ItemCategory(rs.getInt("category_id"),rs.getString(16)),
                        rs.getDouble("cost"),
                        rs.getDouble("price"),
                        rs.getString("currency"),
                        rs.getInt("quantity"),
                        rs.getInt("min_stock"),
                        "Jack Sparrow",
                        rs.getString("description"),
                        true,
                        false,
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
