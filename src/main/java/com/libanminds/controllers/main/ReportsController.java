package com.libanminds.controllers.main;

import com.jfoenix.controls.JFXButton;
import com.libanminds.constants.Views;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {
    private static final PseudoClass activeReport = PseudoClass.getPseudoClass("activeReport");

    @FXML private JFXButton salesBtn;
    @FXML private JFXButton purchasesBtn;
    @FXML private JFXButton customersBtn;
    @FXML private JFXButton suppliersBtn;
    @FXML private JFXButton soldItemsBtn;
    @FXML private JFXButton purchasedItemsBtn;
    @FXML private JFXButton expensesBtn;
    @FXML private JFXButton incomeBtn;
    @FXML private VBox advancedOptions;

    private String currentReportType = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtons();
    }

    private void initButtons() {
        salesBtn.setOnMouseClicked(event -> showAdvancedOptions(Views.SALES_ADVANCED_OPTIONS));
        purchasesBtn.setOnMouseClicked(event -> showAdvancedOptions(Views.PURCHASES_ADVANCED_OPTIONS));
        customersBtn.setOnMouseClicked(event -> showAdvancedOptions(Views.CUSTOMERS_ADVANCED_OPTIONS));
        suppliersBtn.setOnMouseClicked(event -> showAdvancedOptions(Views.SUPPLIERS_ADVANCED_OPTIONS));
        soldItemsBtn.setOnMouseClicked(event -> showAdvancedOptions(Views.SOLD_ITEMS_ADVANCED_OPTIONS));
        purchasedItemsBtn.setOnMouseClicked(event -> showAdvancedOptions(Views.PURCHASED_ITEMS_ADVANCED_OPTIONS));
        expensesBtn.setOnMouseClicked(event -> showAdvancedOptions(Views.EXPENSES_ADVANCED_OPTIONS));
        incomeBtn.setOnMouseClicked(event -> showAdvancedOptions(Views.INCOME_ADVANCED_OPTIONS));
    }

    private void showAdvancedOptions(String type) {
        if (type.equals(currentReportType)) {
            return;
        }
        toggleActiveCss(false); // deactivate current view button
        currentReportType = type;
        toggleActiveCss(true);  // activate new view button
        advancedOptions.getChildren().clear();
        try {
            advancedOptions.getChildren().add(FXMLLoader.load(getClass().getResource(type)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toggleActiveCss(Boolean val) {
        switch (currentReportType) {
            case Views.SALES_ADVANCED_OPTIONS:
                salesBtn.pseudoClassStateChanged(activeReport, val);
                break;
            case Views.PURCHASES_ADVANCED_OPTIONS:
                purchasesBtn.pseudoClassStateChanged(activeReport, val);
                break;
            case Views.CUSTOMERS_ADVANCED_OPTIONS:
                customersBtn.pseudoClassStateChanged(activeReport, val);
                break;
            case Views.SUPPLIERS_ADVANCED_OPTIONS:
                suppliersBtn.pseudoClassStateChanged(activeReport, val);
                break;
            case Views.SOLD_ITEMS_ADVANCED_OPTIONS:
                soldItemsBtn.pseudoClassStateChanged(activeReport, val);
                break;
            case Views.PURCHASED_ITEMS_ADVANCED_OPTIONS:
                purchasedItemsBtn.pseudoClassStateChanged(activeReport, val);
                break;
            case Views.EXPENSES_ADVANCED_OPTIONS:
                expensesBtn.pseudoClassStateChanged(activeReport, val);
                break;
            case Views.INCOME_ADVANCED_OPTIONS:
                incomeBtn.pseudoClassStateChanged(activeReport, val);
                break;
            default:
                break;
        }
    }
}
