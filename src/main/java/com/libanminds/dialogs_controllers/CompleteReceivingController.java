package com.libanminds.dialogs_controllers;

import com.jfoenix.controls.JFXButton;
import com.libanminds.models.Item;
import com.libanminds.models.Receiving;
import com.libanminds.repositories.ItemsRepository;
import com.libanminds.repositories.ReceivingsRepository;
import com.libanminds.utils.HelperFunctions;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class CompleteReceivingController implements Initializable {

    @FXML
    private TableView<Item> itemsTable;

    @FXML
    private TextField receivingDiscountTextField;

    @FXML
    private CheckBox markAsDiscount;

    @FXML
    private Label subtotalText;

    @FXML
    private Label discountText;

    @FXML
    private Label taxesText;

    @FXML
    private Label totalText;

    @FXML
    private Label amountPaidText;

    @FXML
    private TextField newPaymentField;

    @FXML
    private Label remainingAmountLabel;

    @FXML
    private Button saveBtn;

    private double subtotal;
    private double receivingDiscount;
    private double newReceivingDiscount;
    private double taxes;
    private double totalAmount;
    private double amountPaid;
    private double newPayment;
    private double remainingAmount;

    private Receiving receiving;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtonsClicks();
        setTextFieldsListeners();
    }

    public void setReceiving(Receiving receiving) {
        this.receiving = receiving;
        initNumbers();
        updateNumbersUI();
        initializeTable();
        initFilters();
    }

    private void initNumbers() {
        subtotal = receiving.getTotalAmount() + receiving.getDiscount();
        receivingDiscount = receiving.getDiscount();
        totalAmount = receiving.getTotalAmount();
        amountPaid = receiving.getPaidAmount();
        remainingAmount = totalAmount - amountPaid;
    }

    private void initFilters() {
        receivingDiscountTextField.setTextFormatter(HelperFunctions.getUnsignedNumberFilter());
        newPaymentField.setTextFormatter(HelperFunctions.getUnsignedNumberFilter());
    }

    private void initButtonsClicks() {
        saveBtn.setOnMouseClicked((EventHandler<Event>) event -> updateReceiving());
    }

    private void updateReceiving() {
        ReceivingsRepository.completeReceivingPayment(
                receiving.getID(),
                receiving.getSupplierID(),
                markAsDiscount.isSelected()? receivingDiscount + newReceivingDiscount + remainingAmount : receivingDiscount + newReceivingDiscount,
                markAsDiscount.isSelected() ? totalAmount - remainingAmount : totalAmount,
                amountPaid + newPayment,
                newPayment,
                receiving.getCurrency()
        );

        Stage currentStage = (Stage) saveBtn.getScene().getWindow();
        currentStage.close();
    }

    private void initializeTable() {
        TableColumn<Item, String> codeCol = new TableColumn<>("Code");
        TableColumn<Item, String> nameCol = new TableColumn<>("Name");
        TableColumn<Item, String> saleQuantityCol = new TableColumn<>("Quantity");
        TableColumn<Item, String> priceCol = new TableColumn<>("Price");
        TableColumn<Item, String> saleDiscountCol = new TableColumn<>("Discount");
        TableColumn<Item, String> totalPriceCol = new TableColumn<>("Total");

        itemsTable.getColumns().addAll(codeCol, nameCol, saleQuantityCol, priceCol, saleDiscountCol, totalPriceCol);

        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        saleQuantityCol.setCellValueFactory(new PropertyValueFactory<>("saleQuantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("formattedPrice"));
        saleDiscountCol.setCellValueFactory(new PropertyValueFactory<>("formattedDiscount"));
        totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("formattedTotal"));

        itemsTable.setItems(ItemsRepository.getItemsOfReceiving(receiving));
    }

    private void setTextFieldsListeners() {
        newPaymentField.textProperty().addListener((observable, oldValue, newValue) -> {
            newPayment = (newValue.isEmpty() ? 0 : Double.parseDouble(newValue));

            if ((amountPaid + newPayment) > totalAmount) {
                newPaymentField.setText(oldValue);
            }

            recalculateNumbers();
        });

        receivingDiscountTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            newReceivingDiscount = (newValue.isEmpty() ? 0 : Double.parseDouble(newValue));

            if ((receivingDiscount + newReceivingDiscount) > (subtotal - (amountPaid + newPayment))) {
                receivingDiscountTextField.setText(oldValue);
            }

            recalculateNumbers();
        });
    }

    private void recalculateNumbers() {
        totalAmount = subtotal - receivingDiscount + taxes - newReceivingDiscount;
        remainingAmount = totalAmount - amountPaid - newPayment;

        updateNumbersUI();
    }

    private void updateNumbersUI() {
        DecimalFormat formatter = HelperFunctions.getDecimalFormatter();
        subtotalText.setText( formatter.format(subtotal) + " " + receiving.getCurrency());
        discountText.setText(formatter.format(receivingDiscount + newReceivingDiscount) + " " + receiving.getCurrency());
        taxesText.setText(formatter.format(taxes) + " " + receiving.getCurrency());
        amountPaidText.setText(formatter.format(amountPaid) + " " + receiving.getCurrency());
        totalText.setText(formatter.format(totalAmount) + " " + receiving.getCurrency());
        remainingAmountLabel.setText(formatter.format(remainingAmount) + " " + receiving.getCurrency());
    }
}