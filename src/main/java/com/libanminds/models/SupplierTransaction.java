package com.libanminds.models;

import javafx.beans.property.SimpleIntegerProperty;

public class SupplierTransaction extends Transaction {

    public SimpleIntegerProperty supplierID;

    public SupplierTransaction(int transactionID, int supplierID, int invoiceID, double amount, String currency, boolean isRefund, String transactionDate) {
        super(transactionID, invoiceID, amount, currency, isRefund, transactionDate);
        this.supplierID = new SimpleIntegerProperty(supplierID);
    }

    public int getSupplierID() {
        return supplierID.get();
    }

    public void setSupplierID(int supplierID) {
        this.supplierID.set(supplierID);
    }

    public SimpleIntegerProperty supplierIDProperty() {
        return supplierID;
    }
}
