package com.libanminds.dialogs_controllers;

import com.jfoenix.controls.JFXButton;
import com.libanminds.models.Item;
import com.libanminds.models.Receiving;
import com.libanminds.repositories.ItemsRepository;
import com.libanminds.repositories.ReceivingsRepository;
import com.libanminds.repositories.SalesRepository;
import com.libanminds.utils.EditingCell;
import com.libanminds.utils.HelperFunctions;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class ReturnReceivingItemsController implements Initializable {

    @FXML
    private TableView<Item> receivedItemsTable;

    @FXML
    private TableView<Item> returnedItemsTable;

    @FXML
    private Label subtotalText;

    @FXML
    private Label discountCurrencyText;

    @FXML
    private Label taxesText;

    @FXML
    private Label totalText;

    @FXML
    private Label amountPaidText;

    @FXML
    private Label refundAmountText;

    @FXML
    private Label remainingAmountLabel;

    @FXML
    private TextField discountField;

    @FXML
    private JFXButton saveReceiving;

    private Receiving receiving;

    private double subtotal;
    private double salesDiscount;
    private double taxes;
    private double totalAmount;
    private double amountPaid;
    private double amountToRefund;
    private double remainingAmount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtonsClicks();
    }

    public void setReceiving(Receiving receiving) {
        this.receiving = receiving;
        initNumbers();
        updateNumbersUI();
        initializeTables();
    }

    private void initButtonsClicks() {
        saveReceiving.setOnMouseClicked((EventHandler<Event>) event -> SaveChanges());
    }

    private void initializeTables() {

        //RECEIVED ITEMS TABLE START
        TableColumn<Item, String> codeCol = new TableColumn<>("Code");
        TableColumn<Item, String> nameCol = new TableColumn<>("Name");
        TableColumn<Item, String> saleQuantityCol = new TableColumn<>("Quantity");
        TableColumn<Item, String> priceCol = new TableColumn<>("Price");
        TableColumn<Item, String> saleDiscountCol = new TableColumn<>("Discount");
        TableColumn<Item, String> totalPriceCol = new TableColumn<>("Total");

        receivedItemsTable.getColumns().addAll(codeCol, nameCol, saleQuantityCol, priceCol, saleDiscountCol, totalPriceCol);

        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        saleQuantityCol.setCellValueFactory(new PropertyValueFactory<>("saleQuantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("formattedPrice"));
        saleDiscountCol.setCellValueFactory(new PropertyValueFactory<>("formattedDiscount"));
        totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("formattedTotal"));


        receivedItemsTable.setRowFactory(tv -> {
            TableRow<Item> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() >= 2 && (! row.isEmpty()) ) {
                    Item item = row.getItem();
                    if(returnedItemsTable.getItems().contains(item)) {
                        item.incrementReturnedQuantity();
                    }else {
                        item.incrementReturnedQuantity();
                        receivedItemsTable.refresh();
                        returnedItemsTable.getItems().add(item);
                    }
                    returnedItemsTable.refresh();
                    receivedItemsTable.refresh();
                    recalculateNumbers();
                }
            });
            return row ;
        });

        receivedItemsTable.setItems(ItemsRepository.getItemsOfReceiving(receiving));

        //RETURNED ITEMS TABLE START
        codeCol = new TableColumn<>("Code");
        nameCol = new TableColumn<>("Name");
        TableColumn returnedQuantityCol = saleQuantityCol = new TableColumn<>("Quantity");

        returnedItemsTable.getColumns().addAll(codeCol, nameCol, saleQuantityCol);

        Callback<TableColumn, TableCell> cellFactory =
                p -> new EditingCell();

        returnedQuantityCol.setCellValueFactory(
                new PropertyValueFactory<Item, String>("returnedQuantity"));
        returnedQuantityCol.setCellFactory(cellFactory);

        returnedQuantityCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Item, String>>) t -> {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setReturnedQuantity(Integer.parseInt(t.getNewValue()));

                    returnedItemsTable.refresh();
                    receivedItemsTable.refresh();
                    recalculateNumbers();
                }
        );

        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    private void SaveChanges() {
        receiving.setDiscount(salesDiscount);
        receiving.setPaidAmount(amountPaid - amountToRefund);
        receiving.setTotalAmount(totalAmount);
        ReceivingsRepository.returnReceivedItems(receiving, returnedItemsTable.getItems());
        Stage currentStage = (Stage) saveReceiving.getScene().getWindow();
        currentStage.close();
    }

    private void initNumbers() {
        subtotal = receiving.getTotalAmount() + receiving.getDiscount();
        salesDiscount = receiving.getDiscount();
        totalAmount = receiving.getTotalAmount();
        amountPaid = receiving.getPaidAmount();
        amountToRefund = 0;
        remainingAmount = totalAmount - amountPaid;
        discountField.setText(salesDiscount + "");
    }

    private void recalculateNumbers() {

        subtotal = 0;
        for (int i = 0; i < receivedItemsTable.getItems().size(); i++) {
            subtotal += receivedItemsTable.getItems().get(i).getTotal();
        }

        totalAmount = subtotal - salesDiscount + taxes;
        remainingAmount = totalAmount - amountPaid;

        if(remainingAmount < 0) {
            amountToRefund = Math.abs(remainingAmount);
            remainingAmount = 0;
        }

        updateNumbersUI();
    }

    private void updateNumbersUI() {
        DecimalFormat formatter = HelperFunctions.getDecimalFormatter();
        subtotalText.setText( formatter.format(subtotal) + " " + receiving.getCurrency());
        discountCurrencyText.setText(receiving.getCurrency());
        taxesText.setText(formatter.format(taxes) + " " + receiving.getCurrency());
        totalText.setText(formatter.format(totalAmount) + " " + receiving.getCurrency());
        amountPaidText.setText(formatter.format(amountPaid) + " " + receiving.getCurrency());
        refundAmountText.setText(formatter.format(amountToRefund) + " " + receiving.getCurrency());
        remainingAmountLabel.setText(formatter.format(remainingAmount) + " " + receiving.getCurrency());
    }
}