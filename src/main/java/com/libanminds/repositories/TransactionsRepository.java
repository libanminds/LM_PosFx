package com.libanminds.repositories;

import com.libanminds.models.*;
import com.libanminds.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TransactionsRepository {

    public static ObservableList<CustomerTransaction> getCustomerTransactions(Customer customer) {
        String query = "SELECT * FROM customer_transactions where customer_id = " + customer.getID() + " ORDER BY created_at DESC";

        return getCustomerTransactionsFromQuery(query);
    }

    public static ObservableList<SupplierTransaction> getSuppliersTransactions(Supplier supplier) {
        String query = "SELECT * FROM supplier_transactions where supplier_id = " + supplier.getID() + " ORDER BY created_at DESC";

        return getSupplierTransactionsFromQuery(query);
    }

    public static ObservableList<CustomerTransaction> getSaleTransactions(Sale sale) {
        String query = "SELECT * FROM customer_transactions where invoice_id = " + sale.getID() + " ORDER BY created_at DESC";

        return getCustomerTransactionsFromQuery(query);
    }

    public static ObservableList<SupplierTransaction> getReceivingTransactions(Receiving receiving) {
        String query = "SELECT * FROM supplier_transactions where invoice_id = " + receiving.getID() + " ORDER BY created_at DESC";

        return getSupplierTransactionsFromQuery(query);
    }

    private static ObservableList<CustomerTransaction> getCustomerTransactionsFromQuery(String query) {
        ObservableList<CustomerTransaction> data = FXCollections.observableArrayList();

        try {
            Statement statement = DBConnection.instance.getStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                data.add(new CustomerTransaction(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
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

    private static ObservableList<SupplierTransaction> getSupplierTransactionsFromQuery(String query) {
        ObservableList<SupplierTransaction> data = FXCollections.observableArrayList();

        try {
            Statement statement = DBConnection.instance.getStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                data.add(new SupplierTransaction(
                        rs.getInt("id"),
                        rs.getInt("supplier_id"),
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
