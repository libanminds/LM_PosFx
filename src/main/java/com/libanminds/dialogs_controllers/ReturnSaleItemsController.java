package com.libanminds.dialogs_controllers;

import com.jfoenix.controls.JFXButton;
import com.libanminds.models.Item;
import com.libanminds.models.Sale;
import com.libanminds.repositories.ItemsRepository;
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

public class ReturnSaleItemsController implements Initializable {

    @FXML
    private TableView<Item> soldItemsTable;

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
    private JFXButton saveSale;

    private Sale sale;

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

    public void setSale(Sale sale) {
        this.sale = sale;
        initNumbers();
        updateNumbersUI();
        initializeTables();
        initFilters();
    }

    private void initFilters() {
        discountField.setTextFormatter(HelperFunctions.getUnsignedNumberFilter());
    }

    private void initButtonsClicks() {
        saveSale.setOnMouseClicked((EventHandler<Event>) event -> SaveChanges());
    }

    private void initializeTables() {

        //SOLD ITEMS TABLE START
        TableColumn<Item, String> codeCol = new TableColumn<>("Code");
        TableColumn<Item, String> nameCol = new TableColumn<>("Name");
        TableColumn<Item, String> saleQuantityCol = new TableColumn<>("Quantity");
        TableColumn<Item, String> priceCol = new TableColumn<>("Price");
        TableColumn<Item, String> saleDiscountCol = new TableColumn<>("Discount");
        TableColumn<Item, String> totalPriceCol = new TableColumn<>("Total");

        soldItemsTable.getColumns().addAll(codeCol, nameCol, saleQuantityCol, priceCol, saleDiscountCol, totalPriceCol);

        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        saleQuantityCol.setCellValueFactory(new PropertyValueFactory<>("saleQuantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("formattedPrice"));
        saleDiscountCol.setCellValueFactory(new PropertyValueFactory<>("formattedDiscount"));
        totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("formattedTotal"));


        soldItemsTable.setRowFactory( tv -> {
            TableRow<Item> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() >= 2 && (! row.isEmpty()) ) {
                    Item item = row.getItem();
                    if(returnedItemsTable.getItems().contains(item)) {
                        item.incrementReturnedQuantity();
                    }else {
                        item.incrementReturnedQuantity();
                        soldItemsTable.refresh();
                        returnedItemsTable.getItems().add(item);
                    }
                    returnedItemsTable.refresh();
                    soldItemsTable.refresh();
                    recalculateNumbers();
                }
            });
            return row ;
        });

        soldItemsTable.setItems(ItemsRepository.getItemsOfSale(sale));

        //RETURNED ITEMS TABLE START
        codeCol = new TableColumn<>("Code");
        nameCol = new TableColumn<>("Name");
        TableColumn returnedQuantityCol = saleQuantityCol = new TableColumn<>("Quantity");

        returnedItemsTable.getColumns().addAll(codeCol, nameCol, saleQuantityCol);

        Callback<TableColumn, TableCell> cellFactory =
                p -> new EditingCell(true);

        returnedQuantityCol.setCellValueFactory(
                new PropertyValueFactory<Item, String>("returnedQuantity"));
        returnedQuantityCol.setCellFactory(cellFactory);

        returnedQuantityCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Item, String>>) t -> {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setReturnedQuantity(Integer.parseInt(t.getNewValue()));

                    returnedItemsTable.refresh();
                    soldItemsTable.refresh();
                    recalculateNumbers();
                }
        );

        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    private void SaveChanges() {
        sale.setDiscount(salesDiscount);
        sale.setPaidAmount(amountPaid - amountToRefund);
        sale.setTotalAmount(totalAmount);
        SalesRepository.returnSoldItems(sale, returnedItemsTable.getItems());
        Stage currentStage = (Stage) saveSale.getScene().getWindow();
        currentStage.close();
    }

    private void initNumbers() {
        subtotal = sale.getTotalAmount() + sale.getDiscount();
        salesDiscount = sale.getDiscount();
        totalAmount = sale.getTotalAmount();
        amountPaid = sale.getPaidAmount();
        amountToRefund = 0;
        remainingAmount = totalAmount - amountPaid;
        discountField.setText(salesDiscount + "");
    }

    private void recalculateNumbers() {

        subtotal = 0;
        for (int i = 0; i < soldItemsTable.getItems().size(); i++) {
            subtotal += soldItemsTable.getItems().get(i).getTotal();
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
        subtotalText.setText( formatter.format(subtotal) + " " + sale.getCurrency());
        discountCurrencyText.setText(sale.getCurrency());
        taxesText.setText(formatter.format(taxes) + " " + sale.getCurrency());
        totalText.setText(formatter.format(totalAmount) + " " + sale.getCurrency());
        amountPaidText.setText(formatter.format(amountPaid) + " " + sale.getCurrency());
        refundAmountText.setText(formatter.format(amountToRefund) + " " + sale.getCurrency());
        remainingAmountLabel.setText(formatter.format(remainingAmount) + " " + sale.getCurrency());
    }
}