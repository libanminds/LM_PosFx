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

    public String getName() {
        return make.get() + " " + model.get() + " " + year.get();
    }

    public SimpleIntegerProperty ownerIDProperty() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID.set(ownerID);
    }

    public String getMake() {
        return make.get();
    }

    public SimpleStringProperty makeProperty() {
        return make;
    }

    public void setMake(String make) {
        this.make.set(make);
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

    public String getYear() {
        return year.get();
    }

    public SimpleStringProperty yearProperty() {
        return year;
    }

    public void setYear(String year) {
        this.year.set(year);
    }

    public String getCurrentOdometer() {
        return currentOdometer.get();
    }

    public SimpleStringProperty currentOdometerProperty() {
        return currentOdometer;
    }

    public void setCurrentOdometer(String currentOdometer) {
        this.currentOdometer.set(currentOdometer);
    }

    public String getNextServiceOdometer() {
        return nextServiceOdometer.get();
    }

    public SimpleStringProperty nextServiceOdometerProperty() {
        return nextServiceOdometer;
    }

    public void setNextServiceOdometer(String nextServiceOdometer) {
        this.nextServiceOdometer.set(nextServiceOdometer);
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

    public String getPlate() {
        return plate.get();
    }

    public SimpleStringProperty plateProperty() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate.set(plate);
    }
}
