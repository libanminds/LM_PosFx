package com.libanminds.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Pane body;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        String javaVersion = System.getProperty("java.version");
//        String javafxVersion = System.getProperty("javafx.version");
//        label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");
    }

    public void goToDashboard(ActionEvent event) throws IOException {
        body.getChildren().clear();
        body.getChildren().add(FXMLLoader.load(getClass().getResource("/view/dashboard.fxml")));
    }

}