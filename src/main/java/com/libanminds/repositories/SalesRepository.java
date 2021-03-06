package com.libanminds.repositories;

import com.libanminds.models.Customer;
import com.libanminds.models.Item;
import com.libanminds.models.Sale;
import com.libanminds.singletons.DBConnection;
import com.libanminds.singletons.GlobalSettings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SalesRepository {

    public static ObservableList<Sale> getSales() {
        String query = "SELECT * FROM sales LEFT JOIN customers on sales.customer_id = customers.id WHERE total_amount != 0 ORDER BY created_at DESC";

        return getSalesFromQuery(query, false);
    }

    public static Sale getSale(int saleID) {
        String query = "SELECT * FROM sales LEFT JOIN customers on sales.customer_id = customers.id WHERE sales.id = " + saleID;

        return getSalesFromQuery(query, false).get(0);
    }

    public static ObservableList<Sale> getSalesLike(String value) {
        String query = "SELECT * FROM sales LEFT JOIN customers on sales.customer_id = customers.id where  total_amount != 0 and (" +
                " first_name like '%" + value + "%' or" +
                " last_name like '%" + value + "%' or" +
                " type like '%" + value + "%') ORDER BY created_at DESC";

        return getSalesFromQuery(query, false);
    }

    public static boolean completeSalePayment(int saleID, int customerID, double discount, double totalAmount, double paidAmount, double newPayment, String currency) {

        String query = "UPDATE sales SET discount = ?, total_amount = ?, paid_amount = ? where id = ?";
        try {
            PreparedStatement salesStatement = DBConnection.getInstance().getPreparedStatement(query);
            salesStatement.setDouble(1, discount);
            salesStatement.setDouble(2, totalAmount);
            salesStatement.setDouble(3, paidAmount);
            salesStatement.setInt(4, saleID);
            salesStatement.executeUpdate();
            salesStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        if (newPayment > 0) {
            query = "INSERT INTO customer_transactions(customer_id,invoice_id,amount,currency, is_refund) VALUES (?,?,?,?,?)";

            try {
                PreparedStatement salesStatement = DBConnection.getInstance().getPreparedStatement(query);
                salesStatement.setInt(1, customerID);
                salesStatement.setInt(2, saleID);
                salesStatement.setDouble(3, newPayment);
                salesStatement.setString(4, currency);
                salesStatement.setBoolean(5, false);
                salesStatement.executeUpdate();

                salesStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static boolean createSale(List<Item> items, Customer customer, int car_id, double discount, double totalAmount, String currency, double paidAmount, boolean isOfficial, String paymentType) {

        String query = "INSERT INTO sales(customer_id,car_id,discount,tax_id,conversion_rate, total_amount,currency, paid_amount,is_official,type) VALUES (?,?,?,?,?,?,?,?,?,?)";

        int saleID = -1;

        try {
            PreparedStatement salesStatement = DBConnection.getInstance().getPreparedStatement(query, Statement.RETURN_GENERATED_KEYS);
            salesStatement.setInt(1, customer.getID());
            salesStatement.setInt(2, car_id);
            salesStatement.setDouble(3, discount);
            salesStatement.setInt(4, 1);
            salesStatement.setDouble(5, GlobalSettings.fetch().dollarToLbp);
            salesStatement.setDouble(6, totalAmount);
            salesStatement.setString(7, currency);
            salesStatement.setDouble(8, paidAmount);
            salesStatement.setBoolean(9, isOfficial);
            salesStatement.setString(10, paymentType);
            salesStatement.executeUpdate();

            ResultSet rs = salesStatement.getGeneratedKeys();

            if (rs.next())
                saleID = rs.getInt(1);

            rs.close();
            salesStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        if (paidAmount > 0) {
            query = "INSERT INTO customer_transactions(customer_id,invoice_id,amount,currency, is_refund) VALUES (?,?,?,?,?)";

            try {
                PreparedStatement salesStatement = DBConnection.getInstance().getPreparedStatement(query);
                salesStatement.setInt(1, customer.getID());
                salesStatement.setInt(2, saleID);
                salesStatement.setDouble(3, paidAmount);
                salesStatement.setString(4, currency);
                salesStatement.setBoolean(5, false);
                salesStatement.executeUpdate();

                salesStatement.close();

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        for (Item item : items) {
            query = "INSERT INTO sale_items(item_id,item_properties_id,sale_id,quantity,discount) VALUES (?,?,?,?,?)";
            try {
                PreparedStatement statement = DBConnection.getInstance().getPreparedStatement(query);
                statement.setInt(1, item.getID());
                statement.setInt(2, item.getItemPropertiesID());
                statement.setDouble(3, saleID);
                statement.setInt(4, item.getSaleQuantityValue());
                statement.setDouble(5, item.getDiscountValue());
                statement.executeUpdate();

                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            query = "UPDATE items SET quantity = quantity - ? WHERE id = ?";
            try {
                PreparedStatement statement = DBConnection.getInstance().getPreparedStatement(query);
                statement.setInt(1, (item.getStock() - item.getSaleQuantityValue()) < 0 ? item.getStock() : item.getSaleQuantityValue());
                statement.setInt(2, item.getID());
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

    public static boolean returnSoldItems(Sale sale, List<Item> items, double refundedAmount) {
        String query = "UPDATE sales SET discount = ?, total_amount = ?, paid_amount = ? where id = ?";

        try {
            PreparedStatement salesStatement = DBConnection.getInstance().getPreparedStatement(query);
            salesStatement.setDouble(1, sale.getDiscount());
            salesStatement.setDouble(2, sale.getTotalAmount());
            salesStatement.setDouble(3, sale.getPaidAmount());
            salesStatement.setInt(4, sale.getID());
            salesStatement.executeUpdate();
            salesStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        if (refundedAmount > 0) {
            query = "INSERT INTO customer_transactions(customer_id,invoice_id,amount,currency, is_refund) VALUES (?,?,?,?,?)";

            try {
                PreparedStatement salesStatement = DBConnection.getInstance().getPreparedStatement(query);
                salesStatement.setInt(1, sale.getCustomerID());
                salesStatement.setInt(2, sale.getID());
                salesStatement.setDouble(3, refundedAmount);
                salesStatement.setString(4, sale.getCurrency());
                salesStatement.setBoolean(5, true);
                salesStatement.executeUpdate();

                salesStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        for (Item item : items) {
            query = "UPDATE sale_items SET returned_quantity = ? where sale_id = ? AND item_id = ?";
            try {
                PreparedStatement itemStatement = DBConnection.getInstance().getPreparedStatement(query);
                itemStatement.setInt(1, item.getPreviouslyReturnedQuantity() + item.getReturnedQuantityValue());
                itemStatement.setInt(2, sale.getID());
                itemStatement.setInt(3, item.getID());
                itemStatement.executeUpdate();
                itemStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

            query = "UPDATE items SET quantity = quantity + ? WHERE id = ?";
            try {
                PreparedStatement itemStatement = DBConnection.getInstance().getPreparedStatement(query);
                itemStatement.setInt(1, item.getReturnedQuantityValue());
                itemStatement.setInt(2, item.getID());
                itemStatement.executeUpdate();

                itemStatement.close();

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public static ObservableList<Sale> getCompactSalesOfCustomer(int customerID) {

        String query = "SELECT id, total_amount, paid_amount, currency, (total_amount - paid_amount) AS balance FROM sales where customer_id = " + customerID + " ORDER BY balance DESC";

        return getSalesFromQuery(query, true);
    }

    private static ObservableList<Sale> getSalesFromQuery(String query, boolean isCompact) {
        ObservableList<Sale> data = FXCollections.observableArrayList();

        try {
            Statement statement = DBConnection.getInstance().getStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                data.add(isCompact ? new Sale(
                                rs.getInt(1),
                                rs.getDouble(2),
                                rs.getDouble(3),
                                rs.getString(4)
                        ) : new Sale(
                                rs.getInt(1),
                                rs.getInt("customer_id"),
                                rs.getString("first_name") + " " + rs.getString("last_name"),
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
