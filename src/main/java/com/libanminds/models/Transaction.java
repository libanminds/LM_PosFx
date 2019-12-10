package com.libanminds.models;

import com.libanminds.utils.Constants;
import com.libanminds.utils.GlobalSettings;
import com.libanminds.utils.HelperFunctions;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Transaction {

    private SimpleIntegerProperty transactionID;
    private SimpleIntegerProperty invoiceID;
    private SimpleDoubleProperty amount;
    private SimpleStringProperty currency;
    private SimpleBooleanProperty isRefund;
    private SimpleStringProperty transactionDate;

    public Transaction(int transactionID, int invoiceID, double amount, String currency, boolean isRefund, String transactionDate) {
        this.transactionID = new SimpleIntegerProperty(transactionID);
        this.invoiceID = new SimpleIntegerProperty(invoiceID);
        this.amount = new SimpleDoubleProperty(amount);
        this.currency = new SimpleStringProperty(currency);
        this.isRefund = new SimpleBooleanProperty(isRefund);
        this.transactionDate = new SimpleStringProperty(transactionDate);
    }

    public String getAmountWithCurrency() {
        return HelperFunctions.getDecimalFormatter().format(amount.get()) + " " + currency.get();
    }

    public int getTransactionID() {
        return transactionID.get();
    }

    public SimpleIntegerProperty transactionIDProperty() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID.set(transactionID);
    }

    public int getInvoiceID() {
        return invoiceID.get();
    }

    public SimpleIntegerProperty invoiceIDProperty() {
        return invoiceID;
    }

    public void setInvoiceID(int invoiceID) {
        this.invoiceID.set(invoiceID);
    }

    public double getAmount() {
        return amount.get();
    }

    public double getAmount(String currency) {
        if(this.currency.get().equals(currency)) {
            return amount.get();
        } else {
            if(this.currency.get().equals(Constants.DOLLAR_CURRENCY))
                return amount.get() * GlobalSettings.CONVERSION_RATE_FROM_DOLLAR;
            else
                return amount.get() / GlobalSettings.CONVERSION_RATE_FROM_DOLLAR;
        }
    }

    public SimpleDoubleProperty amountProperty() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public String getCurrency() {
        return currency.get();
    }

    public SimpleStringProperty currencyProperty() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency.set(currency);
    }

    public boolean isRefund() {
        return isRefund.get();
    }

    public SimpleBooleanProperty isRefundProperty() {
        return isRefund;
    }

    public void setIsRefund(boolean isRefund) {
        this.isRefund.set(isRefund);
    }

    public String getTransactionDate() {
        return transactionDate.get();
    }

    public SimpleStringProperty transactionDateProperty() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate.set(transactionDate);
    }
}
