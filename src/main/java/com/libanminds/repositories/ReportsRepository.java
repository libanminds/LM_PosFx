package com.libanminds.repositories;

import com.libanminds.models.*;
import com.libanminds.models.reports_models.CompactReportItem;
import com.libanminds.utils.Constants;
import com.libanminds.utils.DBConnection;
import com.libanminds.utils.GlobalSettings;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ReportsRepository {

    //Returns for each item the following: Amount sold, amount returned to customer, and the item's name
    public static ArrayList<CompactReportItem> getQuantitiesOfSoldItems(String dateFrom, String dateTo) {
        String query = "SELECT code, name, SUM(sale_items.quantity) AS quantity_sold, SUM(returned_quantity) AS quantity_returned FROM sale_items LEFT JOIN items ON item_id = items.id WHERE date(sale_items.created_at) >= date('"+dateFrom+"') and date(sale_items.created_at) <= date('"+dateTo+"') GROUP BY item_id ORDER BY name";

        return getCompactReportItemsFromQuery(query, true);
    }

    //Returns for each item the following: Amount bought, amount returned to supplier, and the item's name
    public static ArrayList<CompactReportItem>  getQuantitiesOfPurchasedItems(String dateFrom, String dateTo) {
        String query = "SELECT code, name, SUM(purchase_items.quantity) AS quantity_bought, SUM(returned_quantity) AS quantity_returned FROM purchase_items LEFT JOIN items ON item_id = items.id WHERE date(purchase_items.created_at) >= date('"+ dateFrom+"') and date(purchase_items.created_at) <= date('"+ dateTo+"') GROUP BY item_id ORDER BY name";

        return getCompactReportItemsFromQuery(query, false);
    }

    //Returns for each sale the total amount, returned amount, paid amount, remaining amount and the discount that was made on the sale (not items individually)
    public static ArrayList<Sale> getInfoOfSales(String dateFrom, String dateTo) {
        String query = "SELECT sales.id, total_amount, paid_amount, sales.currency, sales.discount, SUM((returned_quantity * ((CASE WHEN sales.currency != item_properties.currency THEN (CASE WHEN sales.currency = '"+ Constants.DOLLAR_CURRENCY +"' THEN item_properties.price * "+ 1/GlobalSettings.CONVERSION_RATE_FROM_DOLLAR +" ELSE item_properties.price * "+ GlobalSettings.CONVERSION_RATE_FROM_DOLLAR +" END) ELSE item_properties.price END) - sale_items.discount))) AS returned_amount FROM sale_items LEFT JOIN sales ON sale_id = sales.id LEFT JOIN item_properties ON item_properties_id = item_properties.id WHERE date(sales.created_at) >= date('"+dateFrom+"') and date(sales.created_at) <= date('"+dateTo+"') GROUP BY sales.id";

        ArrayList<Sale> data = new ArrayList<>();

        try {
            Statement statement = DBConnection.instance.getStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                data.add(new Sale(
                        rs.getInt(1),
                        rs.getDouble("total_amount"),
                        rs.getDouble("paid_amount"),
                        rs.getDouble("returned_amount"),
                        rs.getString(4)
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return data;
    }

    //Returns for each receiving the total amount, returned amount, paid amount, remaining amount and the discount that was made on the receiving (not items individually)
    public static ArrayList<Receiving> getInfoOfPurchases(String dateFrom, String dateTo) {
        String query = "SELECT purchases.id, total_amount, paid_amount, purchases.currency, purchases.discount, SUM((returned_quantity * ((CASE WHEN purchases.currency != item_properties.currency THEN (CASE WHEN purchases.currency = '"+ Constants.DOLLAR_CURRENCY +"' THEN item_properties.price * "+ 1/GlobalSettings.CONVERSION_RATE_FROM_DOLLAR +" ELSE item_properties.price * "+ GlobalSettings.CONVERSION_RATE_FROM_DOLLAR +" END) ELSE item_properties.price END) - purchase_items.discount))) AS returned_amount FROM purchase_items LEFT JOIN purchases ON receiving_id = purchases.id LEFT JOIN item_properties ON item_properties_id = item_properties.id WHERE date(purchases.created_at) >= date('"+dateFrom+"') and date(purchases.created_at) <= date('"+dateTo+"') GROUP BY purchases.id";

        ArrayList<Receiving> data = new ArrayList<>();

        try {
            Statement statement = DBConnection.instance.getStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                data.add(new Receiving(
                        rs.getInt(1),
                        rs.getDouble("total_amount"),
                        rs.getDouble("paid_amount"),
                        rs.getDouble("returned_amount"),
                        rs.getString(4)
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return data;
    }

    public static ArrayList<Customer> getCustomers(String dateFrom, String dateTo) {
        String query = "SELECT * FROM customers WHERE date(created_at) >= date('"+dateFrom+"') and date(created_at) <= date('"+dateTo+"') ORDER BY created_at";

        return getCustomersFromQuery(query);
    }

    public static ArrayList<Customer> getRegularCustomers(String dateFrom, String dateTo) {
        String query = "SELECT customers.*, COUNT(customer_id) as sales_count FROM sales LEFT JOIN customers ON customers.id = sales.customer_id WHERE date(sales.created_at) >= date('"+dateFrom+"') and date(sales.created_at) <= date('"+dateTo+"') GROUP BY customer_id ORDER BY sales_count DESC";

        return getCustomersFromQuery(query);
    }

    public static ArrayList<CompactReportItem> getItemsBoughtByCustomer(int customerID, String dateFrom, String dateTo) {
        String query = "SELECT items.code, items.name, SUM(sale_items.quantity) AS quantity_bought, SUM(sale_items.returned_quantity) AS quantity_returned FROM sale_items LEFT JOIN items ON item_id = items.id LEFT JOIN sales ON sale_id = sales.id WHERE customer_id = " + customerID + " and date(sale_items.created_at) >= date('"+dateFrom+"') and date(sale_items.created_at) <= date('"+dateTo+"') GROUP BY item_id";

        return getCompactReportItemsFromQuery(query, false);
    }

    public static ArrayList<Supplier> getSuppliers(String dateFrom, String dateTo) {
        String query = "SELECT * FROM suppliers WHERE date(created_at) >= date('"+dateFrom+"') and date(created_at) <= date('"+dateTo+"') ORDER BY created_at";

        return getSuppliersFromQuery(query);
    }

    public static ArrayList<CompactReportItem> getItemsBoughtFromSupplier(int supplierID, String dateFrom, String dateTo) {
        String query = "SELECT items.code, items.name, SUM(purchase_items.quantity) AS quantity_bought, SUM(purchase_items.returned_quantity) AS quantity_returned FROM purchase_items LEFT JOIN items ON item_id = items.id LEFT JOIN purchases ON receiving_id = purchases.id WHERE supplier_id = "+ supplierID +" and date(purchase_items.created_at) >= date('"+dateFrom+"') and date(purchase_items.created_at) <= date('"+dateTo+"') GROUP BY item_id";

        return getCompactReportItemsFromQuery(query, false);
    }

    public static ArrayList<Expense> getExpenses(String dateFrom, String dateTo) {
        String query = "SELECT * FROM expenses LEFT JOIN expense_types ON expenses.type_id = expense_types.id WHERE date(expenses.created_at) >= date('" + dateFrom + "') and date(expenses.created_at) <= date('" + dateTo + "') ORDER BY expenses.created_at";

        ArrayList<Expense> data = new ArrayList<>();
        try {
            Statement statement = DBConnection.instance.getStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                data.add(new Expense(
                        rs.getInt(1),
                        new Type(rs.getInt("type_id"), rs.getString("name")),
                        rs.getString("description"),
                        rs.getDouble("amount"),
                        rs.getString("currency"),
                        rs.getString("payment_type"),
                        rs.getString("recipient"),
                        rs.getString("notes")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return data;
    }

    public static ArrayList<Income> getIncomes(String dateFrom, String dateTo) {
        String query = "SELECT * FROM incomes LEFT JOIN income_types ON incomes.type_id = income_types.id WHERE date(incomes.created_at) >= date('" + dateFrom + "') and date(incomes.created_at) <= date('" + dateTo + "') ORDER BY created_at";

        ArrayList<Income> data = new ArrayList<>();
        try {
            Statement statement = DBConnection.instance.getStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                data.add(new Income(
                        rs.getInt(1),
                        new Type(rs.getInt("type_id"), rs.getString("name")),
                        rs.getString("description"),
                        rs.getDouble("amount"),
                        rs.getString("currency"),
                        rs.getString("payment_type"),
                        rs.getString("taken_from"),
                        rs.getString("notes")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return data;
    }

    private static ArrayList<Customer> getCustomersFromQuery(String query) {
        ArrayList<Customer> data = new ArrayList<>();

        try {
            Statement statement = DBConnection.instance.getStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                data.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("company"),
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

    private static ArrayList<Supplier> getSuppliersFromQuery(String query) {
        ArrayList<Supplier> data = new ArrayList<>();

        try {
            Statement statement = DBConnection.instance.getStatement();
            ResultSet rs = statement.executeQuery(query);
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

    private static ArrayList<CompactReportItem> getCompactReportItemsFromQuery(String query, boolean isSold) {
        ArrayList<CompactReportItem> data = new ArrayList<>();
        try {
            Statement statement = DBConnection.instance.getStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                data.add(new CompactReportItem(
                        rs.getInt("code"),
                        rs.getInt(isSold ? "quantity_sold" : "quantity_bought"),
                        rs.getInt("quantity_returned"),
                        rs.getString("name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
        return data;
    }
}
