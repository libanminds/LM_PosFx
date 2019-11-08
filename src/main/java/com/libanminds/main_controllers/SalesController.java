package com.libanminds.main_controllers;

import com.jfoenix.controls.JFXButton;
import com.libanminds.enums.Currency;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SalesController implements Initializable {

    @FXML
    private TextField customerSelection;
    @FXML
    private Button addCustomer;
    @FXML
    private CheckComboBox<?> carSelection;
    @FXML
    private Button addCar;
    @FXML
    private ComboBox<String> currencySelection;
    @FXML
    private ComboBox<?> modeSelection;
    @FXML
    private Label invoiceNb;
    @FXML
    private TextField itemSelection;
    @FXML
    private Button addItem;
    @FXML
    private TextField itemQuantity;
    @FXML
    private TextField itemDiscount;
    @FXML
    private TableView<?> itemsTable;
    @FXML
    private ComboBox<?> taxSelection;
    @FXML
    private TextField globalDiscount;
    @FXML
    private Label subtotal;
    @FXML
    private Label discount;
    @FXML
    private Label taxes;
    @FXML
    private Label total;
    @FXML
    private JFXButton clearBtn;
    @FXML
    private JFXButton saveBtn;
    @FXML
    private TextField pastInvoiceSearch;
    @FXML
    private TableView<?> pastInvoicesTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        itemQuantity.setOnMouseClicked(e -> itemQuantity.selectAll());
        itemDiscount.setOnMouseClicked(e -> itemDiscount.selectAll());
        currencySelection.setItems(FXCollections.observableArrayList(Currency.getSymbols()));
        currencySelection.setValue(Currency.LBP.getSymbol());
    }
}
