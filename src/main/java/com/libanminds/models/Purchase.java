package com.libanminds.models;

import com.libanminds.utils.HelperFunctions;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Purchase {

    private SimpleIntegerProperty purchaseID;
    private SimpleIntegerProperty supplierID;
    private SimpleStringProperty supplierName;
    private SimpleDoubleProperty discount;
    private SimpleIntegerProperty taxID;
    private SimpleDoubleProperty conversionRate;
    private SimpleDoubleProperty totalAmount;
    private SimpleStringProperty currency;
    private SimpleDoubleProperty paidAmount;
    private SimpleBooleanProperty isOfficial;
    private SimpleStringProperty paymentType;

    //CAREFUL: This value is valid only when the 3rd constructor is called
    private SimpleDoubleProperty returnedAmount;

    public Purchase(int purchaseID, int supplierID, String supplierName, double discount, int taxID, double conversionRate, double totalAmount, String currency, double paidAmount, boolean isOfficial, String paymentType) {
        this.purchaseID = new SimpleIntegerProperty(purchaseID);
        this.supplierID = new SimpleIntegerProperty(supplierID);
        this.supplierName = new SimpleStringProperty(supplierName);
        this.discount = new SimpleDoubleProperty(discount);
        this.taxID = new SimpleIntegerProperty(taxID);
        this.conversionRate = new SimpleDoubleProperty(conversionRate);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
        this.currency = new SimpleStringProperty(currency);
        this.paidAmount = new SimpleDoubleProperty(paidAmount);
        this.isOfficial = new SimpleBooleanProperty(isOfficial);
        this.paymentType = new SimpleStringProperty(paymentType);
    }

    public Purchase(int purchaseID, double totalAmount, double paidAmount, String currency) {
        this.purchaseID = new SimpleIntegerProperty(purchaseID);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
        this.paidAmount = new SimpleDoubleProperty(paidAmount);
        this.currency = new SimpleStringProperty(currency);
    }

    public Purchase(int saleID, double totalAmount, double paidAmount, double returnedAmount, String currency) {
        this.purchaseID = new SimpleIntegerProperty(saleID);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
        this.paidAmount = new SimpleDoubleProperty(paidAmount);
        this.returnedAmount = new SimpleDoubleProperty(returnedAmount);
        this.currency = new SimpleStringProperty(currency);
    }

    public int getID() {
        return purchaseID.get();
    }

    public SimpleIntegerProperty purchaseIDProperty() {
        return purchaseID;
    }

    public void setPurchaseID(int purchaseID) {
        this.purchaseID.set(purchaseID);
    }

    public String getRemainingAmountFormatted() {
        return HelperFunctions.getDecimalFormatter().format(totalAmount.get() - paidAmount.get()) + " " + getCurrency();
    }

    public boolean isComplete() {
        return paidAmount.get() == totalAmount.get();
    }

    public String getFormattedBalance() {
        return HelperFunctions.getDecimalFormatter().format(totalAmount.getValue() - paidAmount.getValue()) + " " + currency.getValue();
    }

    public String getFormattedTotalAmount() {
        return HelperFunctions.getDecimalFormatter().format(totalAmount.getValue()) + " " + currency.getValue();
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

    public String getSupplierName() {
        return supplierName.get();
    }

    public void setSupplierName(String supplierName) {
        this.supplierName.set(supplierName);
    }

    public double getDiscount() {
        return discount.get();
    }

    public void setDiscount(double discount) {
        this.discount.set(discount);
    }

    public String getDiscountFormatted() {
        return HelperFunctions.getDecimalFormatter().format(discount.get()) + " " + currency.get();
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

    public String getTotalAmountFormatted() {
        return HelperFunctions.getDecimalFormatter().format(totalAmount.get()) + " " + currency.get();
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

    public String getPaidAmountFormatted() {
        return HelperFunctions.getDecimalFormatter().format(paidAmount.get()) + " " + currency.get();
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