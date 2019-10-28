package com.libanminds.controllers;

import com.jfoenix.controls.JFXButton;
import com.libanminds.utils.Views;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private static final PseudoClass activeView = PseudoClass.getPseudoClass("activeView");

    @FXML
    private VBox body;
    @FXML
    private JFXButton menuDashboard;
    @FXML
    private JFXButton menuCustomers;
    @FXML
    private JFXButton menuSuppliers;
    @FXML
    private JFXButton menuItems;
    @FXML
    private JFXButton menuReports;
    @FXML
    private JFXButton menuSales;
    @FXML
    private JFXButton menuReceiving;
    @FXML
    private JFXButton menuExpenses;
    @FXML
    private JFXButton menuIncome;
    @FXML
    private JFXButton menuAppointments;
    @FXML
    private JFXButton menuEmployees;
    @FXML
    private JFXButton menuSettings;
    @FXML
    private JFXButton menuNotifications;
    @FXML
    private JFXButton menuLogout;

    private String currentView;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            body.getChildren().add(FXMLLoader.load(getClass().getResource(Views.DASHBOARD)));
            currentView = Views.DASHBOARD;
            menuDashboard.pseudoClassStateChanged(activeView, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initButtons();

    }

    private void initButtons() {
        menuDashboard.setOnAction(actionEvent -> onMenuClick(Views.DASHBOARD));
        menuCustomers.setOnAction(actionEvent -> onMenuClick(Views.CUSTOMERS));
        menuSuppliers.setOnAction(actionEvent -> onMenuClick(Views.SUPPLIERS));
        menuItems.setOnAction(actionEvent -> onMenuClick(Views.ITEMS));
        menuReports.setOnAction(actionEvent -> onMenuClick(Views.REPORTS));
        menuSales.setOnAction(actionEvent -> onMenuClick(Views.SALES));
        menuReceiving.setOnAction(actionEvent -> onMenuClick(Views.RECEIVING));
        menuExpenses.setOnAction(actionEvent -> onMenuClick(Views.EXPENSES));
        menuIncome.setOnAction(actionEvent -> onMenuClick(Views.INCOME));
        menuAppointments.setOnAction(actionEvent -> onMenuClick(Views.APPOINTMENTS));
        menuEmployees.setOnAction(actionEvent -> onMenuClick(Views.EMPLOYEES));
        menuSettings.setOnAction(actionEvent -> onMenuClick(Views.SETTINGS));
        menuNotifications.setOnAction(actionEvent -> onMenuClick(Views.NOTIFICATIONS));
        menuLogout.setOnAction(event -> logout());
    }

    private void onMenuClick(String destination) {
        if (destination.equals(currentView)) {
            return;
        }
        toggleActiveCss(false); // deactivate current view button
        currentView = destination;
        toggleActiveCss(true);  // activate new view button
        body.getChildren().clear();
        try {
            body.getChildren().add(FXMLLoader.load(getClass().getResource(destination)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        //To implement
    }

    private void toggleActiveCss(Boolean val) {
        switch (currentView) {
            case Views.DASHBOARD:
                menuDashboard.pseudoClassStateChanged(activeView, val);
                break;
            case Views.CUSTOMERS:
                menuCustomers.pseudoClassStateChanged(activeView, val);
                break;
            case Views.SUPPLIERS:
                menuSuppliers.pseudoClassStateChanged(activeView, val);
                break;
            case Views.ITEMS:
                menuItems.pseudoClassStateChanged(activeView, val);
                break;
            case Views.REPORTS:
                menuReports.pseudoClassStateChanged(activeView, val);
                break;
            case Views.SALES:
                menuSales.pseudoClassStateChanged(activeView, val);
                break;
            case Views.RECEIVING:
                menuReceiving.pseudoClassStateChanged(activeView, val);
                break;
            case Views.EXPENSES:
                menuExpenses.pseudoClassStateChanged(activeView, val);
                break;
            case Views.INCOME:
                menuIncome.pseudoClassStateChanged(activeView, val);
                break;
            case Views.APPOINTMENTS:
                menuAppointments.pseudoClassStateChanged(activeView, val);
                break;
            case Views.EMPLOYEES:
                menuEmployees.pseudoClassStateChanged(activeView, val);
                break;
            case Views.SETTINGS:
                menuSettings.pseudoClassStateChanged(activeView, val);
                break;
            case Views.NOTIFICATIONS:
                menuNotifications.pseudoClassStateChanged(activeView, val);
                break;
            default:
                break;
        }
    }
}