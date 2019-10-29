package com.libanminds.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomersController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private Button newCustomerBtn;

    @FXML
    private TableView customersTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
