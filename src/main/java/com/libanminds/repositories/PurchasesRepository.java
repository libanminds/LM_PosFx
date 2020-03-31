package com.libanminds.repositories;

import com.libanminds.models.Item;
import com.libanminds.models.Purchase;
import com.libanminds.models.Supplier;
import com.libanminds.singletons.DBConnection;
import com.libanminds.singletons.GlobalSettings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PurchasesRepository {

    public static ObservableList<Purchase> getPurchases() {
        String query = "SELECT * FROM purchases LEFT JOIN suppliers on purchases.supplier_id = suppliers.id WHERE total_amount != 0 ORDER BY created_at DESC";

        return getPurchasesFromQuery(query, false);
    }

    public static Purchase getPurchase(int purchaseID) {
        String query = "SELECT * FROM purchases LEFT JOIN suppliers on purchases.supplier_id = suppliers.id WHERE purchases.id = " + purchaseID;

        return getPurchasesFromQuery(query, false).get(0);
    }

    public static ObservableList<Purchase> getPurchasesLike(String value) {
        String query = "SELECT * FROM purchases LEFT JOIN suppliers on purchases.supplier_id = suppliers.id where  total_amount != 0 and (" +
                " first_name like '%" + value + "%' or" +
                " last_name like '%" + value + "%' or" +
                " type like '%" + value + "%') ORDER BY created_at DESC";

        return getPurchasesFromQuery(query, false);
    }

    public static boolean completePurchasePayment(int purchaseID, int supplierID, double discount, double totalAmount, double paidAmount, double newPayment, String currency) {

        String query = "UPDATE purchases SET discount = ?, total_amount = ?, paid_amount = ? where id = ?";

        try {
            PreparedStatement salesStatement = DBConnection.getInstance().getPreparedStatement(query);
            salesStatement.setDouble(1, discount);
            salesStatement.setDouble(2, totalAmount);
            salesStatement.setDouble(3, paidAmount);
            salesStatement.setInt(4, purchaseID);
            salesStatement.executeUpdate();
            salesStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        if (newPayment > 0) {
            query = "INSERT INTO supplier_transactions(supplier_id,invoice_id,amount,currency, is_refund) VALUES (?,?,?,?,?)";

            try {
                PreparedStatement salesStatement = DBConnection.getInstance().getPreparedStatement(query);
                salesStatement.setInt(1, supplierID);
                salesStatement.setInt(2, purchaseID);
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

    public static boolean createPurchase(List<Item> items, Supplier supplier, double discount, double totalAmount, String currency, double paidAmount, boolean isOfficial, String paymentType) {

        String query = "INSERT INTO purchases(supplier_id,discount,tax_id,conversion_rate, total_amount,currency, paid_amount,is_official,type) VALUES (?,?,?,?,?,?,?,?,?)";

        int purchaseID = -1;

        try {
            PreparedStatement purchasesStatement = DBConnection.getInstance().getPreparedStatement(query, Statement.RETURN_GENERATED_KEYS);
            purchasesStatement.setInt(1, supplier.getID());
            purchasesStatement.setDouble(2, discount);
            purchasesStatement.setInt(3, 1);
            purchasesStatement.setDouble(4, GlobalSettings.fetch().dollarToLbp);
            purchasesStatement.setDouble(5, totalAmount);
            purchasesStatement.setString(6, currency);
            purchasesStatement.setDouble(7, paidAmount);
            purchasesStatement.setBoolean(8, isOfficial);
            purchasesStatement.setString(9, paymentType);
            purchasesStatement.executeUpdate();

            ResultSet rs = purchasesStatement.getGeneratedKeys();

            if (rs.next())
                purchaseID = rs.getInt(1);

            rs.close();
            purchasesStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        if (paidAmount > 0) {
            query = "INSERT INTO supplier_transactions(supplier_id,invoice_id,amount,currency, is_refund) VALUES (?,?,?,?,?)";

            try {
                PreparedStatement purchasesStatement = DBConnection.getInstance().getPreparedStatement(query);
                purchasesStatement.setInt(1, supplier.getID());
                purchasesStatement.setInt(2, purchaseID);
                purchasesStatement.setDouble(3, paidAmount);
                purchasesStatement.setString(4, currency);
                purchasesStatement.setBoolean(5, false);
                purchasesStatement.executeUpdate();

                purchasesStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        for (Item item : items) {
            query = "INSERT INTO purchase_items(item_id,item_properties_id,purchase_id,quantity,discount) VALUES (?,?,?,?,?)";
            try {
                PreparedStatement statement = DBConnection.getInstance().getPreparedStatement(query);
                statement.setInt(1, item.getID());
                statement.setInt(2, item.getItemPropertiesID());
                statement.setDouble(3, purchaseID);
                statement.setInt(4, item.getSaleQuantityValue());
                statement.setDouble(5, item.getDiscountValue());
                statement.executeUpdate();

                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

            query = "UPDATE items SET quantity = quantity + ? WHERE id = ?";
            try {
                PreparedStatement statement = DBConnection.getInstance().getPreparedStatement(query);
                statement.setInt(1, item.getSaleQuantityValue());
                statement.setInt(2, item.getID());
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


    public static boolean returnPurchasedItems(Purchase purchase, List<Item> items, double refundedAmount) {
        String query = "UPDATE purchases SET discount = ?, total_amount = ?, paid_amount = ? where id = ?";
        try {
            PreparedStatement purchasesStatement = DBConnection.getInstance().getPreparedStatement(query);
            purchasesStatement.setDouble(1, purchase.getDiscount());
            purchasesStatement.setDouble(2, purchase.getTotalAmount());
            purchasesStatement.setDouble(3, purchase.getPaidAmount());
            purchasesStatement.setInt(4, purchase.getID());
            purchasesStatement.executeUpdate();
            purchasesStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


        if (refundedAmount > 0) {
            query = "INSERT INTO supplier_transactions(supplier_id,invoice_id,amount,currency, is_refund) VALUES (?,?,?,?,?)";

            try {
                PreparedStatement purchasesStatement = DBConnection.getInstance().getPreparedStatement(query);
                purchasesStatement.setInt(1, purchase.getSupplierID());
                purchasesStatement.setInt(2, purchase.getID());
                purchasesStatement.setDouble(3, refundedAmount);
                purchasesStatement.setString(4, purchase.getCurrency());
                purchasesStatement.setBoolean(5, true);
                purchasesStatement.executeUpdate();

                purchasesStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        for (Item item : items) {
            query = "UPDATE purchase_items SET returned_quantity = ? where purchase_id = ? AND item_id = ?";
            try {
                PreparedStatement itemStatement = DBConnection.getInstance().getPreparedStatement(query);
                itemStatement.setInt(1, item.getPreviouslyReturnedQuantity() + item.getReturnedQuantityValue());
                itemStatement.setInt(2, purchase.getID());
                itemStatement.setInt(3, item.getID());
                itemStatement.executeUpdate();
                itemStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

            query = "UPDATE items SET quantity = quantity - ? WHERE id = ?";
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

    public static ObservableList<Purchase> getCompactPurchaseOfCustomer(int supplierID) {

        String query = "SELECT id, total_amount, paid_amount, currency, (total_amount - paid_amount) AS balance FROM purchases where supplier_id = " + supplierID + " ORDER BY balance DESC";

        return getPurchasesFromQuery(query, true);
    }

    private static ObservableList<Purchase> getPurchasesFromQuery(String query, boolean isCompact) {
        ObservableList<Purchase> data = FXCollections.observableArrayList();

        try {
            Statement statement = DBConnection.getInstance().getStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                data.add(isCompact ? new Purchase(
                                rs.getInt(1),
                                rs.getDouble(2),
                                rs.getDouble(3),
                                rs.getString(4)
                        ) : new Purchase(
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
