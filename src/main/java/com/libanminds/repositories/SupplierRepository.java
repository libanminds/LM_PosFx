package com.libanminds.repositories;

import com.libanminds.models.Supplier;
import com.libanminds.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SupplierRepository {
    public static ObservableList<Supplier> getSuppliers() {
        String query = "SELECT * FROM suppliers";

        return getCustomersFromQuery(query);
    }

    public static ObservableList<Supplier> getSuppliersLike(String value) {
        String query = "SELECT * FROM suppliers where" +
                " first_name like '%" + value + "%' or" +
                " last_name like '%" + value + "%' or" +
                " email like '%" + value + "%' or" +
                " phone like '%"+ value + "%'";

        return getCustomersFromQuery(query);
    }

    public static boolean addSupplier(Supplier supplier) {
        String query = "INSERT INTO suppliers(first_name,last_name,email,phone,company,address,notes) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
        try {
            statement.setString(1, supplier.getFirstName());
            statement.setString(2, supplier.getLastName());
            statement.setString(3, supplier.getEmail());
            statement.setString(4, supplier.getPhone());
            statement.setString(5, supplier.getCompany());
            statement.setString(6, supplier.getAddress());
            statement.setString(7, supplier.getNotes());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateSupplier(Supplier supplier) {
        String query = "UPDATE suppliers SET first_name = ? , last_name = ? , email = ?, phone = ?, company = ? , address = ?, notes = ? where id = ?";

        PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
        try {
            statement.setString(1, supplier.getFirstName());
            statement.setString(2, supplier.getLastName());
            statement.setString(3, supplier.getEmail());
            statement.setString(4, supplier.getPhone());
            statement.setString(5, supplier.getCompany());
            statement.setString(6, supplier.getAddress());
            statement.setString(7, supplier.getNotes());
            statement.setInt(8, supplier.getID());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean updateBalance(int supplierID, double balance) {
        try {
            String query = "UPDATE suppliers SET balance = "+ balance + " WHERE id = " + supplierID;
            Statement statement  = DBConnection.instance.getStatement();
            statement.executeUpdate(query);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static boolean deleteSupplier(Supplier supplier) {
        try {
            String query = "DELETE FROM suppliers where id = " + supplier.getID();
            Statement statement  = DBConnection.instance.getStatement();
            statement.executeUpdate(query);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    private static ObservableList<Supplier> getCustomersFromQuery(String query) {
        ObservableList<Supplier> data = FXCollections.observableArrayList();
        try {
            Statement statement  = DBConnection.instance.getStatement();
            ResultSet rs    = statement.executeQuery(query);
            while (rs.next()) {
                data.add(new Supplier(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("company"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("notes"),
                        rs.getDouble("balance")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return data;
    }
}
