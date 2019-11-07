package com.libanminds.main_controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.libanminds.utils.Views;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void onLogin(ActionEvent event) {
        // TODO login logic
        System.out.println("Username: " + username.getText() + "\nPassword: " + password.getText());
        try {
            redirectToMain();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void redirectToMain() throws IOException {
        stage.close();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource(Views.MAIN)));
        stage.setTitle("POS System");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
