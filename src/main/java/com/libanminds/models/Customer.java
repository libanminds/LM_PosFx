package com.libanminds.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Customer {

    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty email;
    private SimpleStringProperty phone;
    private SimpleStringProperty address;
    private SimpleDoubleProperty balance;

    public Customer(String firstName,String lastName, String email, String phone, String address, double balance) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
        this.address = new SimpleStringProperty(address);
        this.balance = new SimpleDoubleProperty(balance);
    }

    public String getName() {
        return firstName.get() + " " +lastName.get();
    }

    public void setName(String fName, String lName) {
        firstName.set(fName);
        firstName.set(lName);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String fName) {
        email.set(fName);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String val) {
        phone.set(val);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String val) {
        address.set(val);
    }

    public double getBalance() {
        return balance.get();
    }

    public void setBalance(double val) {
        balance.set(val);
    }
}
