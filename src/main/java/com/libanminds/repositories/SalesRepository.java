package com.libanminds.repositories;

import com.libanminds.models.Customer;
import com.libanminds.models.Item;
import com.libanminds.models.Sale;
import com.libanminds.utils.DBConnection;
import com.libanminds.utils.GlobalSettings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SalesRepository {

    public static boolean createSale(List<Item> items, Customer customer, double discount, double totalAmount, String currency, double paidAmount, boolean isOfficial, String paymentType) {

        String query = "INSERT INTO sales(customer_id,discount,tax_id,conversion_rate, total_amount,currency, paid_amount,is_official,type) VALUES (?,?,?,?,?,?,?,?,?)";

        PreparedStatement salesStatement = DBConnection.instance.getPreparedStatement(query, Statement.RETURN_GENERATED_KEYS);

        int saleID = -1;

        try {
            salesStatement.setInt(1, customer.getID());
            salesStatement.setDouble(2, discount);
            salesStatement.setInt(3, 1);
            salesStatement.setDouble(4, GlobalSettings.CONVERSION_RATE_FROM_DOLLAR);
            salesStatement.setDouble(5, totalAmount);
            salesStatement.setString(6, currency);
            salesStatement.setDouble(7, paidAmount);
            salesStatement.setBoolean(8, isOfficial);
            salesStatement.setString(9, paymentType);
            salesStatement.executeUpdate();

            ResultSet rs = salesStatement.getGeneratedKeys();

            if(rs.next())
                saleID = rs.getInt(1);

            rs.close();
            salesStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        for(int i = 0; i < items.size(); i++) {
            query = "INSERT INTO sale_items(item_id,sale_id,quantity,discount) VALUES (?,?,?,?)";
            PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
            try {
                statement.setInt(1, items.get(i).getID());
                statement.setDouble(2, saleID);
                statement.setInt(3, items.get(i).getSaleQuantityValue());
                statement.setDouble(4, items.get(i).getDiscountValue());
                statement.executeUpdate();

                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        double balance = (totalAmount - paidAmount);

            return  balance > 0 ?
                    CustomersRepository.updateBalance(customer.getID(), balance)
                    : true;

    }

    public static ObservableList<Sale> getCompactSalesOfCustomer(int customerID) {

        String query = "SELECT id, total_amount, paid_amount, currency, (total_amount - paid_amount) AS balance FROM sales where customer_id = " + customerID + " ORDER BY balance DESC";

        return getSalesFromQuery(query);
    }

    private static ObservableList<Sale> getSalesFromQuery(String query) {
        ObservableList<Sale> data = FXCollections.observableArrayList();

        try {
            Statement statement  = DBConnection.instance.getStatement();
            ResultSet rs    = statement.executeQuery(query);
            while (rs.next()) {
                data.add(new Sale(
                        rs.getInt(1),
                        rs.getDouble(2),
                        rs.getDouble(3),
                        rs.getString(4)
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return data;
    }
}
