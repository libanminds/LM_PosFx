package com.libanminds.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Car {
    private int id;
    private int ownerId;
    private SimpleStringProperty vin;
    private SimpleStringProperty model;
    private SimpleIntegerProperty year;

    public Car(int id, int ownerId, String vin, String model, int year) {
        this.id = id;
        this.ownerId = ownerId;
        this.vin = new SimpleStringProperty(vin);
        this.model = new SimpleStringProperty(model);
        this.year = new SimpleIntegerProperty(year);
    }

    public int getId() {
        return id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getVin() {
        return vin.get();
    }

    public SimpleStringProperty vinProperty() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin.set(vin);
    }

    public String getModel() {
        return model.get();
    }

    public SimpleStringProperty modelProperty() {
        return model;
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public int getYear() {
        return year.get();
    }

    public SimpleIntegerProperty yearProperty() {
        return year;
    }

    public void setYear(int year) {
        this.year.set(year);
    }
}
