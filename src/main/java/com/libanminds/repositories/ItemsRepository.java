package com.libanminds.repositories;

import com.libanminds.models.*;
import com.libanminds.utils.DBConnection;
import com.libanminds.utils.GlobalSettings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ItemsRepository {
    public static ObservableList<Item> getItems() {
        String query = "SELECT * FROM items LEFT JOIN item_categories ON items.category_id = item_categories.id LEFT JOIN item_properties ON items.item_properties_id = item_properties.id";

        return getItemsFromQuery(query);
    }

    public static ObservableList<Item> getItemsLike(String value) {
        String query = "SELECT * FROM items LEFT JOIN item_categories ON items.category_id = item_categories.id  LEFT JOIN item_properties ON items.item_properties_id = item_properties.id where" +
                " code like '%" + value + "%' or" +
                " items.name like '%" + value + "%' or" +
                " item_categories.name like '%" + value + "%' or" +
                " description like '%" + value + "%'";

        return getItemsFromQuery(query);
    }

    public static ObservableList<Item> getItemsOfSale(Sale sale) {
        String query = "SELECT * FROM items INNER JOIN sale_items ON items.id = sale_items.item_id LEFT JOIN item_properties ON items.item_properties_id = item_properties.id where sale_id = " + sale.getID();

        return getItemsOfSAPFromQuery(query, sale.getCurrency());
    }

    public static ObservableList<Item> getItemsOfReceiving(Receiving receiving) {
        String query = "SELECT * FROM items INNER JOIN purchase_items ON items.id = purchase_items.item_id LEFT JOIN item_properties ON items.item_properties_id = item_properties.id where receiving_id = " + receiving.getID();

        return getItemsOfSAPFromQuery(query, receiving.getCurrency());
    }

    public static boolean addItem(Item item) {
        String query = "INSERT INTO item_properties(cost, price, currency) VALUES (?,?,?)";

        PreparedStatement propertyStatement = DBConnection.instance.getPreparedStatement(query, Statement.RETURN_GENERATED_KEYS);

        int item_properties_id = -1;

        try {
            propertyStatement.setDouble(1, item.getCost());
            propertyStatement.setDouble(2, item.getInitialPrice());
            propertyStatement.setString(3, item.getCurrency());
            propertyStatement.executeUpdate();

            ResultSet rs = propertyStatement.getGeneratedKeys();

            if (rs.next())
                item_properties_id = rs.getInt(1);

            rs.close();
            propertyStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        int item_id = -1;

        query = "INSERT INTO items(code, item_properties_id,image,name,category_id,quantity,description,min_stock,ttc) VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement itemStatement = DBConnection.instance.getPreparedStatement(query, Statement.RETURN_GENERATED_KEYS);
        try {
            itemStatement.setInt(1, item.getCode()); // code
            itemStatement.setInt(2, item_properties_id); // item_properties_id
            itemStatement.setString(3, item.getImageUrl()); // imagePath
            itemStatement.setString(4, item.getName()); // name
            itemStatement.setInt(5, item.getItemCategory().getID()); // category id
            itemStatement.setInt(6, item.getStock()); //quantity
            itemStatement.setString(7, item.getDescription()); //description
            itemStatement.setInt(8, item.getMinStock()); // min stock
            itemStatement.setBoolean(9, item.getPriceIncludesTax()); // price includes tax
            itemStatement.executeUpdate();

            ResultSet rs = itemStatement.getGeneratedKeys();

            if (rs.next())
                item_id = rs.getInt(1);

            rs.close();
            itemStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        query = "INSERT INTO item_property_history VALUES (?,?)";

        PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);

        try {
            statement.setDouble(1, item_id);
            statement.setDouble(2, item_properties_id);
            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean updateItem(Item item) {
        String query = "SELECT * FROM item_properties where id = " + item.getItemPropertiesID();

        int item_properties_id = item.getItemPropertiesID();
        try {
            Statement statement = DBConnection.instance.getStatement();
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                ItemProperties properties = new ItemProperties(
                        rs.getInt("id"),
                        rs.getDouble("cost"),
                        rs.getDouble("price"),
                        rs.getString("currency")
                );
                if (properties.getCost() != item.getCost() || properties.getPrice() != item.getInitialPrice() || !properties.getCurrency().equals(item.getCurrency())) {
                    query = "INSERT INTO item_properties(cost, price, currency) VALUES (?,?,?)";

                    PreparedStatement salesStatement = DBConnection.instance.getPreparedStatement(query, Statement.RETURN_GENERATED_KEYS);

                    try {
                        salesStatement.setDouble(1, item.getCost());
                        salesStatement.setDouble(2, item.getInitialPrice());
                        salesStatement.setString(3, item.getCurrency());
                        salesStatement.executeUpdate();

                        rs = salesStatement.getGeneratedKeys();

                        if (rs.next())
                            item_properties_id = rs.getInt(1);

                        rs.close();
                        salesStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return false;
                    }

                    query = "INSERT INTO item_property_history VALUES (?,?)";

                    PreparedStatement historyStatement = DBConnection.instance.getPreparedStatement(query);

                    try {
                        historyStatement.setDouble(1, item.getID());
                        historyStatement.setDouble(2, item_properties_id);
                        historyStatement.executeUpdate();

                    } catch (SQLException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        query = "UPDATE items SET code = ?, item_properties_id = ? , image = ? , name = ?, category_id = ?, quantity = ?, description = ?, min_stock = ?, ttc = ? where id = ?";

        PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
        try {
            statement.setInt(1, item.getCode()); // code
            statement.setInt(2, item_properties_id); //
            statement.setString(3, item.getImageUrl()); // imagePath
            statement.setString(4, item.getName()); // name
            statement.setInt(5, item.getItemCategory().getID()); // category id
            statement.setInt(6, item.getStock()); //quantity
            statement.setString(7, item.getDescription()); //description
            statement.setInt(8, item.getMinStock()); // min stock
            statement.setBoolean(9, item.getPriceIncludesTax()); // price includes tax
            statement.setInt(10, item.getID()); // price i
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteItem(Item item) {
        try {
            String query = "DELETE FROM item_properties WHERE id IN(SELECT property_id from item_property_history WHERE item_id = " + item.getID() + ")";
            Statement statement = DBConnection.instance.getStatement();
            statement.executeUpdate(query);

            query = "DELETE FROM item_property_history where item_id = " + item.getID();
            statement = DBConnection.instance.getStatement();
            statement.executeUpdate(query);

            query = "DELETE FROM items where id = " + item.getID();
            statement = DBConnection.instance.getStatement();
            statement.executeUpdate(query);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //NOTE: Column indexes are used when the result have duplicate column names.
    private static ObservableList<Item> getItemsFromQuery(String query) {
        ObservableList<Item> data = FXCollections.observableArrayList();
        try {
            Statement statement = DBConnection.instance.getStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                data.add(new Item(
                        rs.getInt(1),
                        rs.getString("image"),
                        rs.getInt("code"),
                        rs.getInt("item_properties_id"),
                        rs.getString(5),
                        new ItemCategory(rs.getInt("category_id"), rs.getString(16)),
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

    private static ObservableList<Item> getItemsOfSAPFromQuery(String query, String currency) {
        ObservableList<Item> data = FXCollections.observableArrayList();
        try {
            Statement statement = DBConnection.instance.getStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {

                Item item = new Item(
                        rs.getInt(1),
                        rs.getString("image"),
                        rs.getInt("code"),
                        rs.getInt(3),
                        rs.getString("name"),
                        new ItemCategory(rs.getInt("category_id"), rs.getString(16)),
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
                );
                item.setSaleQuantity(rs.getInt(17));
                item.setDiscount(rs.getInt("discount"));
                item.setCurrency(currency);

                data.add(item);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return data;
    }
}
