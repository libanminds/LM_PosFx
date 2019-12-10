package com.libanminds.repositories;

import com.libanminds.models.Customer;
import com.libanminds.models.CustomerTransaction;
import com.libanminds.models.Supplier;
import com.libanminds.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TransactionsRepository {

    public static ObservableList<CustomerTransaction> getCustomerTransactions(Customer customer) {
        String query = "SELECT * FROM customer_transactions where customer_id = " + customer.getID() + " ORDER BY created_at DESC";

        return getTransactionsFromQuery(query,"customer_id");
    }

    public static ObservableList<CustomerTransaction> getSuppliersTransactions(Supplier supplier) {
        String query = "SELECT * FROM supplier_transactions where supplier_id = " + supplier.getID() + " ORDER BY created_at DESC";

        return getTransactionsFromQuery(query,"supplier_id");
    }

    private static ObservableList<CustomerTransaction> getTransactionsFromQuery(String query, String owner) {
        ObservableList<CustomerTransaction> data = FXCollections.observableArrayList();

        try {
            Statement statement  = DBConnection.instance.getStatement();
            ResultSet rs    = statement.executeQuery(query);
            while (rs.next()) {
                data.add(new CustomerTransaction(
                        rs.getInt("id"),
                        rs.getInt(owner),
                        rs.getInt("invoice_id"),
                        rs.getDouble("amount"),
                        rs.getString("currency"),
                        rs.getBoolean("is_refund"),
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
