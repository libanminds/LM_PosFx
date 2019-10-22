package com.libanminds.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private Label labelDash;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelDash.setText("I am Dashboard");
    }
}
