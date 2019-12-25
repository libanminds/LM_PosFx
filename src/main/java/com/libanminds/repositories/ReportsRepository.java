package com.libanminds.repositories;

import com.libanminds.utils.Constants;
import com.libanminds.utils.GlobalSettings;

public class ReportsRepository {

    //Returns for each item the following: Amount sold, amount returned to customer, and the item's name
    public void getQuantitiesOfSoldItems() {
        String query = "SELECT code, name, SUM(sale_items.quantity) AS quantity_sold, SUM(returned_quantity) AS quantity_returned FROM sale_items LEFT JOIN items ON item_id = items.id GROUP BY item_id ORDER BY name";
    }

    //Returns for each item the following: Amount bought, amount returned to supplier, and the item's name
    public void getQuantitiesOfPurchasedItems() {
        String query = "SELECT code, name, SUM(purchase_items.quantity) AS quantity_bought, SUM(returned_quantity) AS quantity_returned FROM purchase_items LEFT JOIN items ON item_id = items.id GROUP BY item_id ORDER BY name";
    }

    //Returns for each sale the total amount, returned amount, paid amount, remaining amount and the discount that was made on the sale (not items individually)
    public void getInfoOfSales() {
        String query = "SELECT sales.id, total_amount, paid_amount, sales.currency, sales.discount, SUM((returned_quantity * ((CASE WHEN sales.currency != item_properties.currency THEN (CASE WHEN sales.currency = '"+ Constants.DOLLAR_CURRENCY +"' THEN item_properties.price * "+ 1/GlobalSettings.CONVERSION_RATE_FROM_DOLLAR +" ELSE item_properties.price * "+ GlobalSettings.CONVERSION_RATE_FROM_DOLLAR +" END) ELSE item_properties.price END) - sale_items.discount))) AS returned_amount FROM sale_items LEFT JOIN sales ON sale_id = sales.id LEFT JOIN item_properties ON item_properties_id = item_properties.id GROUP BY sales.id";
    }

    //Returns for each receiving the total amount, returned amount, paid amount, remaining amount and the discount that was made on the receiving (not items individually)
    public void getInfoOfReceivings() {
        String query = "SELECT purchases.id, total_amount, paid_amount, purchases.currency, purchases.discount, SUM((returned_quantity * ((CASE WHEN purchases.currency != item_properties.currency THEN (CASE WHEN purchases.currency = '"+ Constants.DOLLAR_CURRENCY +"' THEN item_properties.price * "+ 1/GlobalSettings.CONVERSION_RATE_FROM_DOLLAR +" ELSE item_properties.price * "+ GlobalSettings.CONVERSION_RATE_FROM_DOLLAR +" END) ELSE item_properties.price END) - purchase_items.discount))) AS returned_amount FROM purchase_items LEFT JOIN purchases ON receiving_id = sales.id LEFT JOIN item_properties ON item_properties_id = item_properties.id GROUP BY purchases.id";
    }

    public static void getCustomers() {
        String query = "SELECT * FROM customers ORDER BY created_at";
    }

    public static void getRegularCustomers() {
        String query = "SELECT customers.*, COUNT(customer_id) as sales_count FROM sales LEFT JOIN customers ON customers.id = sales.customer_id GROUP BY customer_id ORDER BY sales_count DESC";
    }

    public static void getItemsBoughtByCustomer() {
        String query = "SELECT items.code, items.name, SUM(sale_items.quantity) AS quantity_bought, SUM(sale_items.returned_quantity) AS quantity_returned FROM sale_items LEFT JOIN items ON item_id = items.id LEFT JOIN sales ON sale_id = sales.id WHERE customer_id = 1 GROUP BY item_id";
    }

    public static void getSuppliers() {
        String query = "SELECT * FROM suppliers ORDER BY created_at";
    }

    public static void getItemsBoughtFromSupplier() {
        String query = "SELECT items.code, items.name, SUM(purchase_items.quantity) AS quantity_bought, SUM(purchase_items.returned_quantity) AS quantity_returned FROM purchase_items LEFT JOIN items ON item_id = items.id LEFT JOIN purchases ON receiving_id = purchases.id WHERE supplier_id = 1 GROUP BY item_id";
    }

    public static void getExpenses() {
        String query = "SELECT * from expenses ORDER BY created_at";
    }

    public static void getIncomes() {
        String query = "SELECT * from incomes ORDER BY created_at";
    }
}
