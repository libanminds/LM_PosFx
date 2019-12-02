package com.libanminds.dialogs_controllers;

import com.jfoenix.controls.JFXButton;
import com.libanminds.models.Item;
import com.libanminds.models.Sale;
import com.libanminds.repositories.ItemsRepository;
import com.libanminds.repositories.SalesRepository;
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

public class ViewSaleController implements Initializable {

    @FXML
    private TableView<Item> itemsTable;

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
    private Label remainingAmountLabel;

    private double subtotal;
    private double salesDiscount;
    private double taxes;
    private double totalAmount;
    private double amountPaid;
    private double remainingAmount;

    private Sale sale;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void setSale(Sale sale) {
        this.sale = sale;
        initNumbers();
        updateNumbersUI();
        initializeTable();
    }

    private void initNumbers() {
        subtotal = sale.getTotalAmount() + sale.getDiscount();
        salesDiscount = sale.getDiscount();
        totalAmount = sale.getTotalAmount();
        amountPaid = sale.getPaidAmount();
        remainingAmount = totalAmount - amountPaid;
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

        itemsTable.setItems(ItemsRepository.getItemsOfSale(sale));
    }

    private void updateNumbersUI() {
        DecimalFormat formatter = HelperFunctions.getDecimalFormatter();
        subtotalText.setText( formatter.format(subtotal) + " " + sale.getCurrency());
        discountText.setText(formatter.format(salesDiscount) + " " + sale.getCurrency());
        taxesText.setText(formatter.format(taxes) + " " + sale.getCurrency());
        amountPaidText.setText(formatter.format(amountPaid) + " " + sale.getCurrency());
        totalText.setText(formatter.format(totalAmount) + " " + sale.getCurrency());
        remainingAmountLabel.setText(formatter.format(remainingAmount) + " " + sale.getCurrency());
    }
}