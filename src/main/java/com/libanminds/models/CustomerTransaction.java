package com.libanminds.models;

import javafx.beans.property.SimpleIntegerProperty;

public class CustomerTransaction extends Transaction {

    public SimpleIntegerProperty customerID;

    public CustomerTransaction(int transactionID, int customerID, int invoiceID, double amount, String currency, boolean isRefund, String transactionDate) {
        super(transactionID, invoiceID, amount, currency, isRefund, transactionDate);
        this.customerID = new SimpleIntegerProperty(customerID);
    }

    public int getCustomerID() {
        return customerID.get();
    }

    public void setCustomerID(int customerID) {
        this.customerID.set(customerID);
    }

    public SimpleIntegerProperty customerIDProperty() {
        return customerID;
    }
}
