package com.libanminds.models;

import com.libanminds.utils.HelperFunctions;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Receiving {
    private SimpleIntegerProperty receivingID;
    private SimpleIntegerProperty supplierID;
    private SimpleDoubleProperty discount;
    private SimpleIntegerProperty taxID;
    private SimpleDoubleProperty conversionRate;
    private SimpleDoubleProperty totalAmount;
    private SimpleStringProperty currency;
    private SimpleDoubleProperty paidAmount;
    private SimpleBooleanProperty isOfficial;
    private SimpleStringProperty paymentType;

    public Receiving(int receivingID, int supplierID, double discount, int taxID, double conversionRate, double totalAmount, String currency, double paidAmount, boolean isOfficial, String paymentType) {
        this.receivingID = new SimpleIntegerProperty(receivingID);
        this.supplierID = new SimpleIntegerProperty(supplierID);
        this.supplierID = new SimpleIntegerProperty(supplierID);
        this.discount = new SimpleDoubleProperty(discount);
        this.taxID = new SimpleIntegerProperty(taxID);
        this.conversionRate = new SimpleDoubleProperty(conversionRate);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
        this.currency = new SimpleStringProperty(currency);
        this.paidAmount = new SimpleDoubleProperty(paidAmount);
        this.isOfficial = new SimpleBooleanProperty(isOfficial);
        this.paymentType = new SimpleStringProperty(paymentType);
    }

    public Receiving(int receivingID, double totalAmount, double paidAmount, String currency) {
        this.receivingID = new SimpleIntegerProperty(receivingID);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
        this.paidAmount = new SimpleDoubleProperty(paidAmount);
        this.currency = new SimpleStringProperty(currency);
    }

    public String getFormattedBalance() {
        return HelperFunctions.getDecimalFormatter().format(totalAmount.getValue() - paidAmount.getValue())+ " " + currency.getValue();
    }

    public String getFormattedTotalAmount() {
        return HelperFunctions.getDecimalFormatter().format(totalAmount.getValue()) + " " + currency.getValue();
    }

    public int getSupplierID() {
        return supplierID.get();
    }

    public SimpleIntegerProperty supplierIDProperty() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID.set(supplierID);
    }

    public double getDiscount() {
        return discount.get();
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
