package com.libanminds.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Car {

    private int id;
    private SimpleIntegerProperty ownerID;
    private SimpleStringProperty make;
    private SimpleStringProperty model;
    private SimpleStringProperty year;
    private SimpleStringProperty currentOdometer;
    private SimpleStringProperty nextServiceOdometer;
    private SimpleStringProperty vin;
    private SimpleStringProperty plate;


    public Car(int id, int ownerID, String make, String model, String year, String currentOdometer, String nextServiceOdometer, String vin, String plate) {
        this.id = id;
        this.ownerID = new SimpleIntegerProperty(ownerID);
        this.make = new SimpleStringProperty(make);
        this.model = new SimpleStringProperty(model);
        this.year = new SimpleStringProperty(year);
        this.currentOdometer = new SimpleStringProperty(currentOdometer);
        this.nextServiceOdometer = new SimpleStringProperty(nextServiceOdometer);
        this.vin = new SimpleStringProperty(vin);
        this.plate = new SimpleStringProperty(plate);
    }

    public int getID() {
        return id;
    }

    public int getOwnerID() {
        return ownerID.get();
    }

    public void setOwnerID(int ownerID) {
        this.ownerID.set(ownerID);
    }

    public String getName() {
        return make.get() + " " + model.get() + " " + year.get();
    }

    public SimpleIntegerProperty ownerIDProperty() {
        return ownerID;
    }

    public String getMake() {
        return make.get();
    }

    public void setMake(String make) {
        this.make.set(make);
    }

    public SimpleStringProperty makeProperty() {
        return make;
    }

    public String getModel() {
        return model.get();
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public SimpleStringProperty modelProperty() {
        return model;
    }

    public String getYear() {
        return year.get();
    }

    public void setYear(String year) {
        this.year.set(year);
    }

    public SimpleStringProperty yearProperty() {
        return year;
    }

    public String getCurrentOdometer() {
        return currentOdometer.get();
    }

    public void setCurrentOdometer(String currentOdometer) {
        this.currentOdometer.set(currentOdometer);
    }

    public SimpleStringProperty currentOdometerProperty() {
        return currentOdometer;
    }

    public String getNextServiceOdometer() {
        return nextServiceOdometer.get();
    }

    public void setNextServiceOdometer(String nextServiceOdometer) {
        this.nextServiceOdometer.set(nextServiceOdometer);
    }

    public SimpleStringProperty nextServiceOdometerProperty() {
        return nextServiceOdometer;
    }

    public String getVin() {
        return vin.get();
    }

    public void setVin(String vin) {
        this.vin.set(vin);
    }

    public SimpleStringProperty vinProperty() {
        return vin;
    }

    public String getPlate() {
        return plate.get();
    }

    public void setPlate(String plate) {
        this.plate.set(plate);
    }

    public SimpleStringProperty plateProperty() {
        return plate;
    }
}
