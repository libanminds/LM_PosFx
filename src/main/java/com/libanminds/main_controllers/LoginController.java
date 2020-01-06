package com.libanminds.main_controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.libanminds.repositories.UsersRepository;
import com.libanminds.utils.Views;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.jar.Manifest;

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
        boolean isAuthenticated = UsersRepository.login(username.getText(), password.getText());
        System.out.println("Username: " + username.getText() + "\nPassword: " + password.getText());
        System.out.println("Authenticated: " + isAuthenticated);

        //TODO: check isAuthentication

        try {
            redirectToMain();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void redirectToMain() throws IOException {
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.MAIN));
        Scene scene = new Scene(loader.load());
        ((MainController) loader.getController()).setStage(stage);
        stage.setTitle("POS System");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
