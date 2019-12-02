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
import java.util.List;

public class ReceivingsRepository {

    public static ObservableList<Receiving> getReceivings() {
        String query = "SELECT * FROM purchases LEFT JOIN suppliers on purchases.supplier_id = suppliers.id";

        return getReceivingsFromQuery(query,false);
    }

    public static ObservableList<Receiving> getReceivingsLike(String value) {
        String query = "SELECT * FROM purchases LEFT JOIN suppliers on purchases.supplier_id = suppliers.id where" +
                " first_name like '%" + value + "%' or" +
                " last_name like '%" + value + "%' or" +
                " type like '%"+ value + "%'";

        return getReceivingsFromQuery(query,false);
    }

    public static boolean completeReceivingPayment(int receivingID,double discount, double totalAmount, double paidAmount) {

        String query = "UPDATE purchases SET discount = ?, total_amount = ?, paid_amount = ? where id = ?";
        PreparedStatement salesStatement = DBConnection.instance.getPreparedStatement(query);

        try {
            salesStatement.setDouble(1, discount);
            salesStatement.setDouble(2, totalAmount);
            salesStatement.setDouble(3, paidAmount);
            salesStatement.setInt(4, receivingID);
            salesStatement.executeUpdate();
            salesStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createReceiving(List<Item> items, Supplier supplier, double discount, double totalAmount, String currency, double paidAmount, boolean isOfficial, String paymentType) {

        String query = "INSERT INTO purchases(supplier_id,discount,tax_id,conversion_rate, total_amount,currency, paid_amount,is_official,type) VALUES (?,?,?,?,?,?,?,?,?)";

        PreparedStatement receivingsStatement = DBConnection.instance.getPreparedStatement(query, Statement.RETURN_GENERATED_KEYS);

        int receivingID = -1;

        try {
            receivingsStatement.setInt(1, supplier.getID());
            receivingsStatement.setDouble(2, discount);
            receivingsStatement.setInt(3, 1);
            receivingsStatement.setDouble(4, GlobalSettings.CONVERSION_RATE_FROM_DOLLAR);
            receivingsStatement.setDouble(5, totalAmount);
            receivingsStatement.setString(6, currency);
            receivingsStatement.setDouble(7, paidAmount);
            receivingsStatement.setBoolean(8, isOfficial);
            receivingsStatement.setString(9, paymentType);
            receivingsStatement.executeUpdate();

            ResultSet rs = receivingsStatement.getGeneratedKeys();

            if(rs.next())
                receivingID = rs.getInt(1);

            rs.close();
            receivingsStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        for (Item item : items) {
            query = "INSERT INTO purchase_items(item_id,receiving_id,quantity,discount) VALUES (?,?,?,?)";
            PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
            try {
                statement.setInt(1, item.getID());
                statement.setDouble(2, receivingID);
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

            return !(balance > 0) || SupplierRepository.updateBalance(supplier.getID(), balance);
    }

    public static ObservableList<Receiving> getCompactReceivingOfCustomer(int supplierID) {

        String query = "SELECT id, total_amount, paid_amount, currency, (total_amount - paid_amount) AS balance FROM purchases where supplier_id = " + supplierID + " ORDER BY balance DESC";

        return getReceivingsFromQuery(query,true);
    }

    private static ObservableList<Receiving> getReceivingsFromQuery(String query, boolean isCompact) {
        ObservableList<Receiving> data = FXCollections.observableArrayList();

        try {
            Statement statement  = DBConnection.instance.getStatement();
            ResultSet rs    = statement.executeQuery(query);
            while (rs.next()) {
                data.add(isCompact ?  new Receiving(
                        rs.getInt(1),
                        rs.getDouble(2),
                        rs.getDouble(3),
                        rs.getString(4)
                ): new Receiving(
                        rs.getInt(1),
                        rs.getInt("supplier_id"),
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
