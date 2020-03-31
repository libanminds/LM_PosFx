package com.libanminds.controllers.main;

import com.jfoenix.controls.JFXButton;
import com.libanminds.constants.AuthorizationKeys;
import com.libanminds.constants.Views;
import com.libanminds.utils.Authorization;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private static final PseudoClass activeView = PseudoClass.getPseudoClass("activeView");
    public static MainController instance;

    @FXML private VBox menu;
    @FXML private VBox body;
    @FXML private JFXButton menuDashboard;
    @FXML private JFXButton menuCustomers;
    @FXML private JFXButton menuSuppliers;
    @FXML private JFXButton menuItems;
    @FXML private JFXButton menuReports;
    @FXML private JFXButton menuSales;
    @FXML private JFXButton menuNewSale;
    @FXML private JFXButton menuPurchases;
    @FXML private JFXButton menuNewPurchase;
    @FXML private JFXButton menuExpenses;
    @FXML private JFXButton menuIncome;
    @FXML private JFXButton menuEmployees;
    @FXML private JFXButton menuSettings;
    @FXML private JFXButton menuLogout;

    private String currentView;
    private Stage stage;

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

        if (Authorization.cannot(AuthorizationKeys.VIEW_DASHBOARD)) {
            menu.getChildren().remove(menuDashboard);
        } else if (initialView.isBlank()) {
            initialView = Views.DASHBOARD;
        }
        if (Authorization.cannot(AuthorizationKeys.VIEW_CUSTOMERS)) {
            menu.getChildren().remove(menuCustomers);
        } else if (initialView.isBlank()) {
            initialView = Views.CUSTOMERS;
        }
        if (Authorization.cannot(AuthorizationKeys.VIEW_SUPPLIERS)) {
            menu.getChildren().remove(menuSuppliers);
        } else if (initialView.isBlank()) {
            initialView = Views.SUPPLIERS;
        }
        if (Authorization.cannot(AuthorizationKeys.VIEW_ITEMS)) {
            menu.getChildren().remove(menuItems);
        } else if (initialView.isBlank()) {
            initialView = Views.ITEMS;
        }
        if (Authorization.cannot(AuthorizationKeys.VIEW_REPORTS)) {
            menu.getChildren().remove(menuReports);
        } else if (initialView.isBlank()) {
            initialView = Views.REPORTS;
        }
        if (Authorization.cannot(AuthorizationKeys.VIEW_SALES)) {
            menu.getChildren().remove(menuSales);
        } else if (initialView.isBlank()) {
            initialView = Views.SALES;
        }
        if (Authorization.cannot(AuthorizationKeys.CREATE_SALE)) {
            menu.getChildren().remove(menuNewSale);
        } else if (initialView.isBlank()) {
            initialView = Views.NEW_SALE;
        }
        if (Authorization.cannot(AuthorizationKeys.VIEW_PURCHASES)) {
            menu.getChildren().remove(menuPurchases);
        } else if (initialView.isBlank()) {
            initialView = Views.PURCHASES;
        }
        if (Authorization.cannot(AuthorizationKeys.CREATE_PURCHASE)) {
            menu.getChildren().remove(menuNewPurchase);
        } else if (initialView.isBlank()) {
            initialView = Views.NEW_PURCHASE;
        }
        if (Authorization.cannot(AuthorizationKeys.VIEW_EXPENSES)) {
            menu.getChildren().remove(menuExpenses);
        } else if (initialView.isBlank()) {
            initialView = Views.EXPENSES;
        }
        if (Authorization.cannot(AuthorizationKeys.VIEW_INCOMES)) {
            menu.getChildren().remove(menuIncome);
        } else if (initialView.isBlank()) {
            initialView = Views.INCOME;
        }
        if (Authorization.cannot(AuthorizationKeys.VIEW_USERS)) {
            menu.getChildren().remove(menuEmployees);
        } else if (initialView.isBlank()) {
            initialView = Views.EMPLOYEES;
        }
        if (Authorization.cannot(AuthorizationKeys.VIEW_SETTINGS)) {
            menu.getChildren().remove(menuSettings);
        } else if (initialView.isBlank()) {
            initialView = Views.SETTINGS;
        }

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
        menuPurchases.setOnAction(actionEvent -> onMenuClick(Views.PURCHASES));
        menuNewPurchase.setOnAction(actionEvent -> onMenuClick(Views.NEW_PURCHASE));
        menuExpenses.setOnAction(actionEvent -> onMenuClick(Views.EXPENSES));
        menuIncome.setOnAction(actionEvent -> onMenuClick(Views.INCOME));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(destination));
            body.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        Platform.exit();
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
            case Views.PURCHASES:
                menuPurchases.pseudoClassStateChanged(activeView, val);
                break;
            case Views.NEW_PURCHASE:
                menuNewPurchase.pseudoClassStateChanged(activeView, val);
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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }
}