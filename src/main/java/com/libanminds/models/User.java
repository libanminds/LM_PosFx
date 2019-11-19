package com.libanminds.models;

import javafx.beans.property.SimpleStringProperty;

public class User {

    private int id;
    private SimpleStringProperty username;
    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty password;
    private SimpleStringProperty email;
    private SimpleStringProperty phone;
    private SimpleStringProperty role;
    private SimpleStringProperty address;

    public User(int id,String username, String firstName, String lastName, String password, String email, String phone, String role, String address) {
        this.id = id;
        this.username = new SimpleStringProperty(username);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.password = new SimpleStringProperty(password);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
        this.role = new SimpleStringProperty(role);
        this.address = new SimpleStringProperty(address);
    }

    public int getID() {
        return id;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String val) {
        username.set(val);
    }

    public String getName() {
        return firstName.get() + " " + lastName.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String val) {
        firstName.set(val);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String val) {
        lastName.set(val);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String val) {
        password.set(val);
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

    public String getRole() {
        return role.get();
    }

    public void setRole(String val) {
        role.set(val);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String val) {
        address.set(val);
    }
}
