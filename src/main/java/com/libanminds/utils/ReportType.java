package com.libanminds.utils;

public enum ReportType {
    ITEMS_SOLD("Items Sold"),
    ITEMS_PURCHASED("Items Purchased"),
    SALES("Sales"),
    PURCHASES("Purchases"),
    INCOME("Income"),
    EXPENSES("Expenses"),
    CUSTOMERS("Customers"),
    ITEMS_PER_CUSTOMER("Items Per Customer"),
    CUSTOMER_TRANSACTIONS("Customer Transactions"),
    SUPPLIERS("Suppliers"),
    ITEMS_PER_SUPPLIER("Items Per Supplier"),
    SUPPLIER_TRANSACTIONS("Supplier Transactions");

    private  String title;

    ReportType(String title) {
        this.title = title;
    }


    @Override
    public String toString() {
        return title;
    }
}
