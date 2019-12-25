package com.libanminds.main_controllers;

import com.jfoenix.controls.JFXButton;
import com.libanminds.utils.Authorization;
import com.libanminds.utils.AuthorizationKeys;
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
    public static MainController instance;
    @FXML
    private VBox menu;
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
    private JFXButton menuNewSale;
    @FXML
    private JFXButton menuReceivings;
    @FXML
    private JFXButton menuNewReceiving;
    @FXML
    private JFXButton menuExpenses;
    @FXML
    private JFXButton menuIncome;
    //    @FXML
//    private JFXButton menuAppointments;
    @FXML
    private JFXButton menuEmployees;
    @FXML
    private JFXButton menuSettings;
    @FXML
    private JFXButton menuLogout;
    private String currentView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        try {
            String initialView = handleAccess();
            body.getChildren().add(FXMLLoader.load(getClass().getResource(initialView)));
            currentView = initialView;
            menuDashboard.pseudoClassStateChanged(activeView, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initButtons();
    }

    private String handleAccess() {
        String initialView = "";

        if (!Authorization.authorized.contains(AuthorizationKeys.CAN_VIEW_DASHBOARD)) {
            menu.getChildren().remove(menuDashboard);
        } else if (initialView.isBlank())
            initialView = Views.DASHBOARD;

        if (!Authorization.authorized.contains(AuthorizationKeys.CAN_VIEW_CUSTOMERS)) {
            menu.getChildren().remove(menuCustomers);
        } else if (initialView.isBlank())
            initialView = Views.CUSTOMERS;

        if (!Authorization.authorized.contains(AuthorizationKeys.CAN_VIEW_SUPPLIERS)) {
            menu.getChildren().remove(menuSuppliers);
        } else if (initialView.isBlank())
            initialView = Views.SUPPLIERS;

        if (!Authorization.authorized.contains(AuthorizationKeys.CAN_VIEW_ITEMS)) {
            menu.getChildren().remove(menuItems);
        } else if (initialView.isBlank())
            initialView = Views.ITEMS;

        if (!Authorization.authorized.contains(AuthorizationKeys.CAN_VIEW_REPORTS)) {
            menu.getChildren().remove(menuReports);
        } else if (initialView.isBlank())
            initialView = Views.REPORTS;

        if (!Authorization.authorized.contains(AuthorizationKeys.CAN_VIEW_SALES)) {
            menu.getChildren().remove(menuSales);
        } else if (initialView.isBlank())
            initialView = Views.SALES;

        if (!Authorization.authorized.contains(AuthorizationKeys.CAN_CREATE_SALE)) {
            menu.getChildren().remove(menuNewSale);
        } else if (initialView.isBlank())
            initialView = Views.NEW_SALE;

        if (!Authorization.authorized.contains(AuthorizationKeys.CAN_VIEW_RECEIVINGS)) {
            menu.getChildren().remove(menuReceivings);
        } else if (initialView.isBlank())
            initialView = Views.RECEIVINGS;

        if (!Authorization.authorized.contains(AuthorizationKeys.CAN_CREATE_RECEIVING)) {
            menu.getChildren().remove(menuNewReceiving);
        } else if (initialView.isBlank())
            initialView = Views.NEW_RECEIVING;

        if (!Authorization.authorized.contains(AuthorizationKeys.CAN_VIEW_EXPENSES)) {
            menu.getChildren().remove(menuExpenses);
        } else if (initialView.isBlank())
            initialView = Views.EXPENSES;

        if (!Authorization.authorized.contains(AuthorizationKeys.CAN_VIEW_INCOMES)) {
            menu.getChildren().remove(menuIncome);
        } else if (initialView.isBlank())
            initialView = Views.INCOME;

        if (!Authorization.authorized.contains(AuthorizationKeys.CAN_VIEW_USERS)) {
            menu.getChildren().remove(menuEmployees);
        } else if (initialView.isBlank())
            initialView = Views.EMPLOYEES;

        if (!Authorization.authorized.contains(AuthorizationKeys.CAN_VIEW_SETTINGS)) {
            menu.getChildren().remove(menuSettings);
        } else if (initialView.isBlank())
            initialView = Views.SETTINGS;

        return initialView;
    }

    private void initButtons() {

        menuDashboard.setOnAction(actionEvent -> onMenuClick(Views.DASHBOARD));
        menuCustomers.setOnAction(actionEvent -> onMenuClick(Views.CUSTOMERS));
        menuSuppliers.setOnAction(actionEvent -> onMenuClick(Views.SUPPLIERS));
        menuItems.setOnAction(actionEvent -> onMenuClick(Views.ITEMS));
        menuReports.setOnAction(actionEvent -> onMenuClick(Views.REPORTS));
        menuSales.setOnAction(actionEvent -> onMenuClick(Views.SALES));
        menuNewSale.setOnAction(actionEvent -> onMenuClick(Views.NEW_SALE));
        menuReceivings.setOnAction(actionEvent -> onMenuClick(Views.RECEIVINGS));
        menuNewReceiving.setOnAction(actionEvent -> onMenuClick(Views.NEW_RECEIVING));
        menuExpenses.setOnAction(actionEvent -> onMenuClick(Views.EXPENSES));
        menuIncome.setOnAction(actionEvent -> onMenuClick(Views.INCOME));
//        menuAppointments.setOnAction(actionEvent -> onMenuClick(Views.APPOINTMENTS));
        menuEmployees.setOnAction(actionEvent -> onMenuClick(Views.EMPLOYEES));
        menuSettings.setOnAction(actionEvent -> onMenuClick(Views.SETTINGS));
        menuLogout.setOnAction(event -> logout());
    }

    public void onMenuClick(String destination) {
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
            case Views.NEW_SALE:
                menuNewSale.pseudoClassStateChanged(activeView, val);
                break;
            case Views.RECEIVINGS:
                menuReceivings.pseudoClassStateChanged(activeView, val);
                break;
            case Views.NEW_RECEIVING:
                menuNewReceiving.pseudoClassStateChanged(activeView, val);
                break;
            case Views.EXPENSES:
                menuExpenses.pseudoClassStateChanged(activeView, val);
                break;
            case Views.INCOME:
                menuIncome.pseudoClassStateChanged(activeView, val);
                break;
            case Views.EMPLOYEES:
                menuEmployees.pseudoClassStateChanged(activeView, val);
                break;
            case Views.SETTINGS:
                menuSettings.pseudoClassStateChanged(activeView, val);
                break;
            default:
                break;
        }
    }
}