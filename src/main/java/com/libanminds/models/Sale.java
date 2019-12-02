package com.libanminds.models;

import com.libanminds.utils.HelperFunctions;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Sale {
    private SimpleIntegerProperty saleID;
    private SimpleIntegerProperty customerID;
    private SimpleStringProperty customerName;
    private SimpleDoubleProperty discount;
    private SimpleIntegerProperty taxID;
    private SimpleDoubleProperty conversionRate;
    private SimpleDoubleProperty totalAmount;
    private SimpleStringProperty currency;
    private SimpleDoubleProperty paidAmount;
    private SimpleBooleanProperty isOfficial;
    private SimpleStringProperty paymentType;

    public Sale(int saleID, int customerID, String customerName, double discount, int taxID, double conversionRate, double totalAmount, String currency, double paidAmount, boolean isOfficial, String paymentType) {
        this.saleID = new SimpleIntegerProperty(saleID);
        this.customerID = new SimpleIntegerProperty(customerID);
        this.customerName = new SimpleStringProperty(customerName);
        this.discount = new SimpleDoubleProperty(discount);
        this.taxID = new SimpleIntegerProperty(taxID);
        this.conversionRate = new SimpleDoubleProperty(conversionRate);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
        this.currency = new SimpleStringProperty(currency);
        this.paidAmount = new SimpleDoubleProperty(paidAmount);
        this.isOfficial = new SimpleBooleanProperty(isOfficial);
        this.paymentType = new SimpleStringProperty(paymentType);
    }

    public Sale(int saleID, double totalAmount, double paidAmount, String currency) {
        this.saleID = new SimpleIntegerProperty(saleID);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
        this.paidAmount = new SimpleDoubleProperty(paidAmount);
        this.currency = new SimpleStringProperty(currency);
    }

    public String getFormattedBalance() {
        return HelperFunctions.getDecimalFormatter().format(totalAmount.getValue() - paidAmount.getValue())+ " " + currency.getValue();
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public String getRemainingAmountFormatted() {
        return HelperFunctions.getDecimalFormatter().format(totalAmount.get() - paidAmount.get()) + " " + getCurrency();
    }

    public int getID() {
        return saleID.get();
    }

    public void setSaleID(int saleID) {
        this.saleID.set(saleID);
    }

    public boolean isComplete() {
        return paidAmount.get() == totalAmount.get();
    }

    public String getFormattedTotalAmount() {
        return HelperFunctions.getDecimalFormatter().format(totalAmount.getValue()) + " " + currency.getValue();
    }

    public int getCustomerID() {
        return customerID.get();
    }

    public SimpleIntegerProperty customerIDProperty() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID.set(customerID);
    }

    public double getDiscount() {
        return discount.get();
    }

    public String getDiscountFormatted() {
        return HelperFunctions.getDecimalFormatter().format(discount.get()) + " " + currency.get();
    }

    public void setDiscount(double discount) {
        this.discount.set(discount);
    }

    public int getTaxID() {
        return taxID.get();
    }

    public void setTaxID(int taxID) {
        this.taxID.set(taxID);
    }

    public double getConversionRate() {
        return conversionRate.get();
    }

    public void setConversionRate(double conversionRate) {
        this.conversionRate.set(conversionRate);
    }

    public double getTotalAmount() {
        return totalAmount.get();
    }

    public String getTotalAmountFormatted() {
        return HelperFunctions.getDecimalFormatter().format(totalAmount.get()) + " " + currency.get();
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount.set(totalAmount);
    }

    public String getCurrency() {
        return currency.get();
    }

    public void setCurrency(String currency) {
        this.currency.set(currency);
    }

    public double getPaidAmount() {
        return paidAmount.get();
    }

    public String getPaidAmountFormatted() {
        return HelperFunctions.getDecimalFormatter().format(paidAmount.get()) + " " + currency.get();
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount.set(paidAmount);
    }

    public boolean isIsOfficial() {
        return isOfficial.get();
    }

    public void setIsOfficial(boolean isOfficial) {
        this.isOfficial.set(isOfficial);
    }

    public String getPaymentType() {
        return paymentType.get();
    }

    public void setPaymentType(String paymentType) {
        this.paymentType.set(paymentType);
    }
}
