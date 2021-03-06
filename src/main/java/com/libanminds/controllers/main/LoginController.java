package com.libanminds.controllers.main;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.libanminds.constants.Views;
import com.libanminds.repositories.UsersRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML private JFXTextField username;
    @FXML private JFXPasswordField password;

    private Stage stage;
    private Alert authAlert = new Alert(Alert.AlertType.WARNING, "Wrong username or password!");

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void onLogin(ActionEvent event) {
        // TODO FIX
//        boolean isAuthenticated = UsersRepository.login(username.getText(), password.getText());
        boolean isAuthenticated = UsersRepository.login("admin", "admin");
        try {
            if (isAuthenticated) redirectToMain();
            else authAlert.show();
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
