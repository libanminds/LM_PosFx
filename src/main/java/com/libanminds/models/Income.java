package com.libanminds.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Income {
    private int id;
    private Type type;
    private SimpleStringProperty description;
    private SimpleDoubleProperty amount;
    private SimpleStringProperty currency;
    private SimpleStringProperty paymentType;
    private SimpleStringProperty from;
    private SimpleStringProperty notes;

    public Income(int id,Type type,String description,double amount,String currency,String paymentType,String recipient, String notes) {
        this.id = id;
        this.type = type;
        this.description = new SimpleStringProperty(description);
        this.amount = new SimpleDoubleProperty(amount);
        this.currency = new SimpleStringProperty(currency);
        this.paymentType = new SimpleStringProperty(paymentType);
        this.from = new SimpleStringProperty(recipient);
        this.notes = new SimpleStringProperty(notes);
    }

    public Type getTypeObject() {
        return type;
    }

    public int getID() {
        return id;
    }

    public String getType() {
        return type.getName();
    }

    public void setType(String val) {
        type.setName(val);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String val) {
        description.set(val);
    }

    public String getAmountWithCurrency() {
        return amount.get() + " "+ currency.get();
    }

    public double getAmount() {
        return amount.get();
    }

    public void setAmount(double val) {
        amount.set(val);
    }

    public String getCurrency() {
        return currency.get();
    }

    public void setCurrency(String val) {
        currency.set(val);
    }

    public String getPaymentType() {
        return paymentType.get();
    }

    public void setPaymentType(String val) {
        paymentType.set(val);
    }

    public String getFrom() {
        return from.get();
    }

    public void setFrom(String val) {
        from.set(val);
    }

    public String getNotes() {
        return notes.get();
    }

    public void setNotes(String val) {
        notes.set(val);
    }
}
