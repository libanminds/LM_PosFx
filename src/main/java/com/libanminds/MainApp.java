package com.libanminds;

import com.libanminds.main_controllers.LoginController;
import com.libanminds.models.reports_models.CompactReportItem;
import com.libanminds.repositories.ReportsRepository;
import com.libanminds.utils.DBConnection;
import com.libanminds.utils.Views;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;


public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        //Initialize Database Connection
        DBConnection.instance = new DBConnection();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.LOGIN));
        Parent root = loader.load();
        ((LoginController) loader.getController()).setStage(stage);
        Scene scene = new Scene(root);
        stage.setTitle("POS System - Login");
        stage.setScene(scene);
        stage.show();
    }

}