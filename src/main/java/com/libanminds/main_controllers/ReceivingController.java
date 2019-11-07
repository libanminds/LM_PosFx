package com.libanminds.main_controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ReceivingController implements Initializable {

    @FXML
    private TextField itemQtty;

    @FXML
    private TextField itemDiscount;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        itemQtty.setOnMouseClicked(e -> itemQtty.selectAll());
        itemDiscount.setOnMouseClicked(e -> itemDiscount.selectAll());
    }

}