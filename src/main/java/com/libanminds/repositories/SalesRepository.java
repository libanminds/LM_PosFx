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

    public static ObservableList<Sale> getSales() {
        String query = "SELECT * FROM sales LEFT JOIN customers on sales.customer_id = customers.id";

        return getSalesFromQuery(query,false);
    }

    public static ObservableList<Sale> getSalesLike(String value) {
        String query = "SELECT * FROM sales LEFT JOIN customers on sales.customer_id = customers.id where" +
                " first_name like '%" + value + "%' or" +
                " last_name like '%" + value + "%' or" +
                " type like '%"+ value + "%'";

        return getSalesFromQuery(query,false);
    }

    public static boolean completeSalePayment(int saleID,double discount, double totalAmount, double paidAmount) {

        String query = "UPDATE sales SET discount = ?, total_amount = ?, paid_amount = ? where id = ?";
        PreparedStatement salesStatement = DBConnection.instance.getPreparedStatement(query);

        try {
            salesStatement.setDouble(1, discount);
            salesStatement.setDouble(2, totalAmount);
            salesStatement.setDouble(3, paidAmount);
            salesStatement.setInt(4, saleID);
            salesStatement.executeUpdate();
            salesStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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

        for (Item item : items) {
            query = "INSERT INTO sale_items(item_id,sale_id,quantity,discount) VALUES (?,?,?,?)";
            PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
            try {
                statement.setInt(1, item.getID());
                statement.setDouble(2, saleID);
                statement.setInt(3, item.getSaleQuantityValue());
                statement.setDouble(4, item.getDiscountValue());
                statement.executeUpdate();

                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        double balance = (totalAmount - paidAmount);

        return !(balance > 0) || CustomersRepository.updateBalance(customer.getID(), balance);

    }

    public static ObservableList<Sale> getCompactSalesOfCustomer(int customerID) {

        String query = "SELECT id, total_amount, paid_amount, currency, (total_amount - paid_amount) AS balance FROM sales where customer_id = " + customerID + " ORDER BY balance DESC";

        return getSalesFromQuery(query,true);
    }

    private static ObservableList<Sale> getSalesFromQuery(String query, boolean isCompact) {
        ObservableList<Sale> data = FXCollections.observableArrayList();

        try {
            Statement statement  = DBConnection.instance.getStatement();
            ResultSet rs    = statement.executeQuery(query);
            while (rs.next()) {
                data.add(isCompact ? new Sale(
                        rs.getInt(1),
                        rs.getDouble(2),
                        rs.getDouble(3),
                        rs.getString(4)
                ):new Sale(
                        rs.getInt(1),
                        rs.getInt("customer_id"),
                        rs.getString("first_name") + rs.getString("last_name"),
                        rs.getDouble("discount"),
                        rs.getInt("tax_id"),
                        rs.getDouble("conversion_rate"),
                        rs.getDouble("total_amount"),
                        rs.getString("currency"),
                        rs.getDouble("paid_amount"),
                        rs.getBoolean("is_official"),
                        rs.getString("type"))
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return data;
    }
}
