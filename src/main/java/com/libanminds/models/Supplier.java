package com.libanminds.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Supplier {

    private SimpleStringProperty name;
    private SimpleStringProperty company;
    private SimpleStringProperty email;
    private SimpleStringProperty phone;
    private SimpleStringProperty address;
    private SimpleDoubleProperty balance;

    public Supplier(String name, String company, String email, String phone, String address, double balance) {
        this.name = new SimpleStringProperty(name);
        this.company = new SimpleStringProperty(company);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
        this.address = new SimpleStringProperty(address);
        this.balance = new SimpleDoubleProperty(balance);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String val) {
        name.set(val);
    }

    public String getCompany() {
        return company.get();
    }

    public void setCompany(String val) {
        company.set(val);
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
