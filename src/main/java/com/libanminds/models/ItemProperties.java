package com.libanminds.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ItemProperties {

    private SimpleIntegerProperty ID;
    private SimpleDoubleProperty cost;
    private SimpleDoubleProperty price;
    private SimpleStringProperty currency;

    public ItemProperties(int ID, double cost, double price, String currency) {
        this.ID = new SimpleIntegerProperty(ID);
        this.cost = new SimpleDoubleProperty(cost);
        this.price = new SimpleDoubleProperty(price);
        this.currency = new SimpleStringProperty(currency);
    }

    public int getID() {
        return ID.get();
    }

    public void setID(int ID) {
        this.ID.set(ID);
    }

    public SimpleIntegerProperty IDProperty() {
        return ID;
    }

    public double getCost() {
        return cost.get();
    }

    public void setCost(double cost) {
        this.cost.set(cost);
    }

    public SimpleDoubleProperty costProperty() {
        return cost;
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public String getCurrency() {
        return currency.get();
    }

    public void setCurrency(String currency) {
        this.currency.set(currency);
    }

    public SimpleStringProperty currencyProperty() {
        return currency;
    }
}
