package com.libanminds.main_controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    // General Tab
    @FXML
    private JFXButton backupDbBtn;
    @FXML
    private JFXCheckBox chooseQttyInSale;
    @FXML
    private JFXCheckBox chooseQttyInPurchases;
    @FXML
    private JFXCheckBox disableCompleteSaleConfirmation;
    @FXML
    private JFXCheckBox disableCompletePurchaseConfirmation;
    @FXML
    private JFXCheckBox sellOutOfStock;
    @FXML
    private TextField minStockQtty;
    @FXML
    private JFXButton discardPrefsBtn;
    @FXML
    private JFXButton savePrefsBtn;

    // Transactions Tab
    @FXML
    private TextField tvaPercentage;
    @FXML
    private JFXButton discardTaxesBtn;
    @FXML
    private JFXButton saveTaxesBtn;
    @FXML
    private TextField dollarToLbp;
    @FXML
    private JFXRadioButton defaultLbp;
    @FXML
    private ToggleGroup defaultCurrency;
    @FXML
    private JFXRadioButton defaultUsd;
    @FXML
    private JFXButton discardCurrencyBtn;
    @FXML
    private JFXButton saveCurrencyBtn;
    @FXML
    private JFXRadioButton defaultCash;
    @FXML
    private ToggleGroup defaultPaymentMethod;
    @FXML
    private JFXRadioButton defaultCheck;
    @FXML
    private JFXButton discardPaymentsBtn;
    @FXML
    private JFXButton savePaymentsBtn;

    // Receipt Tab
    @FXML
    private JFXCheckBox removeCustomerNameFromReceipt;
    @FXML
    private JFXCheckBox removeEmployeeNameFromReceipt;
    @FXML
    private JFXCheckBox hideBarcodeReceipt;
    @FXML
    private JFXButton discardReceiptBtn;
    @FXML
    private JFXButton saveReceiptBtn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initGeneralTab();
        initTransactionsTab();
        initReceiptTab();
    }

    private void initReceiptTab() {

    }

    private void initTransactionsTab() {

    }

    private void initGeneralTab() {

    }
}
