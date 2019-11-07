package com.libanminds.repositories;

import com.libanminds.models.Customer;
import com.libanminds.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomersRepository {
    public static ObservableList<Customer> getCustomers() {
        String query = "SELECT * FROM customers";

        return getCustomersFromQuery(query);
    }

    public static ObservableList<Customer> getCustomersLike(String value) {
        String query = "SELECT * FROM customers where" +
                " first_name like '%" + value + "%' or" +
                " last_name like '%" + value + "%' or" +
                " email like '%" + value + "%' or" +
                " phone like '%"+ value + "%'";

        return getCustomersFromQuery(query);
    }

    public static void addCustomer(Customer customer) {

    }

    private static ObservableList<Customer> getCustomersFromQuery(String query) {
        ObservableList<Customer> data = FXCollections.observableArrayList();

        try {
            Statement statement  = DBConnection.instance.getStatement();
            ResultSet rs    = statement.executeQuery(query);
            while (rs.next()) {
                data.add(new Customer(
                        rs.getString("first_name") + " " + rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        0
                        //rs.getDouble("balance")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return data;
    }
}
