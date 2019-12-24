package com.libanminds.repositories;

import com.libanminds.models.Customer;
import com.libanminds.models.GraphPoint;
import com.libanminds.utils.Constants;
import com.libanminds.utils.DBConnection;
import com.libanminds.utils.GlobalSettings;
import com.libanminds.utils.HelperFunctions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

public class DashboardRepository {

    private static double revenue;

    public static ArrayList<GraphPoint> getPointsOfGraph(String currency) {
        String query = "SELECT SUM(CASE WHEN currency != '" + currency + "' THEN total_amount * " + (currency.equals(Constants.DOLLAR_CURRENCY) ? 1 / GlobalSettings.CONVERSION_RATE_FROM_DOLLAR : GlobalSettings.CONVERSION_RATE_FROM_DOLLAR) + " ELSE total_amount END) AS total, date(created_at) as sale_date  FROM sales GROUP BY sale_date ORDER BY sale_date DESC LIMIT " + GlobalSettings.DAYS_COUNT_OF_SALES_GRAPH;
        return getGraphPointsFromQuery(query);
    }

    public static String getSalesOfToday(String currency) {
        revenue = 0;
        String query = "SELECT SUM(CASE WHEN currency != '" + currency + "' THEN total_amount * " + (currency.equals(Constants.DOLLAR_CURRENCY) ? 1 / GlobalSettings.CONVERSION_RATE_FROM_DOLLAR : GlobalSettings.CONVERSION_RATE_FROM_DOLLAR) + " ELSE total_amount END) AS total, date(created_at) AS sale_date FROM sales WHERE sale_date = date('now') GROUP BY sale_date";
        return getFormattedMoneyFromQuery(query, currency, "sale");
    }

    public static String getExpensesOfToday(String currency) {
        String query = "SELECT SUM(CASE WHEN currency != '" + currency + "' THEN amount * " + (currency.equals(Constants.DOLLAR_CURRENCY) ? 1 / GlobalSettings.CONVERSION_RATE_FROM_DOLLAR : GlobalSettings.CONVERSION_RATE_FROM_DOLLAR) + " ELSE amount END) AS total, date(created_at) AS expense_date FROM expenses WHERE expense_date = date('now') GROUP BY expense_date";

        return getFormattedMoneyFromQuery(query, currency, "expense");
    }

    public static String getIncomesOfToday(String currency) {
        String query = "SELECT SUM(CASE WHEN currency != '" + currency + "' THEN amount * " + (currency.equals(Constants.DOLLAR_CURRENCY) ? 1 / GlobalSettings.CONVERSION_RATE_FROM_DOLLAR : GlobalSettings.CONVERSION_RATE_FROM_DOLLAR) + " ELSE amount END) AS total, date(created_at) AS income_date FROM incomes WHERE income_date = date('now') GROUP BY income_date";

        return getFormattedMoneyFromQuery(query, currency, "income");
    }

    public static String getRevenueOfToday(String currency) {
        return HelperFunctions.getDecimalFormatter().format(revenue) + " " + currency;
    }

    public static ObservableList<Customer> getRegularCustomers() {
        String query = "SELECT customers.*, COUNT(customer_id) as sales_count FROM sales LEFT JOIN customers ON customers.id = sales.customer_id GROUP BY customer_id ORDER BY sales_count DESC";

        return getCustomersFromQuery(query);
    }

    public static ArrayList<GraphPoint> getMostSoldItems() {
        String query = "SELECT items.name, COUNT(item_id) as items_sales_count FROM sale_items LEFT JOIN items ON items.id = sale_items.item_id WHERE sale_items.created_at >= date('now','-7 day') GROUP BY item_id ORDER BY items_sales_count DESC LIMIT 7";

        return getPiChartPointsFromQuery(query);
    }

    private static ObservableList<Customer> getCustomersFromQuery(String query) {
        ObservableList<Customer> data = FXCollections.observableArrayList();

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
                        //rs.getDouble("balance")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return data;
    }

    private static String getFormattedMoneyFromQuery(String query, String currency, String origin) {
        try {
            Statement statement = DBConnection.instance.getStatement();
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                if (origin.equals("sale") || origin.equals("income"))
                    revenue += rs.getDouble("total");
                else
                    revenue -= rs.getDouble("total");

                return HelperFunctions.getDecimalFormatter().format(rs.getDouble("total")) + " " + currency;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return HelperFunctions.getDecimalFormatter().format(0) + " " + currency;
    }

    private static ArrayList<GraphPoint> getGraphPointsFromQuery(String query) {
        ArrayList<GraphPoint> graphDataPoints = new ArrayList<>();

        try {
            Statement statement = DBConnection.instance.getStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                graphDataPoints.add(new GraphPoint(
                        rs.getDouble("total"),
                        rs.getString("sale_date")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        Collections.reverse(graphDataPoints);
        return graphDataPoints;
    }

    private static ArrayList<GraphPoint> getPiChartPointsFromQuery(String query) {
        ArrayList<GraphPoint> graphDataPoints = new ArrayList<>();

        try {
            Statement statement = DBConnection.instance.getStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                graphDataPoints.add(new GraphPoint(
                        rs.getDouble("items_sales_count"),
                        rs.getString("name")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        Collections.reverse(graphDataPoints);
        return graphDataPoints;
    }
}
