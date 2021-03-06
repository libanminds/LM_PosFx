package com.libanminds;

import com.libanminds.constants.Views;
import com.libanminds.controllers.main.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.LOGIN));
        Parent root = loader.load();
        ((LoginController) loader.getController()).setStage(stage);
        Scene scene = new Scene(root);
        stage.setTitle("POS System - Login");
        stage.setScene(scene);
        stage.show();
    }
}
