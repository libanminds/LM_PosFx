package com.libanminds.dialogs_controllers;

import com.libanminds.models.Item;
import com.libanminds.models.Receiving;
import com.libanminds.repositories.ItemsRepository;
import com.libanminds.utils.HelperFunctions;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class ViewReceivingController implements Initializable {

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
    private double receivingDiscount;
    private double taxes;
    private double totalAmount;
    private double amountPaid;
    private double remainingAmount;

    private Receiving receiving;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void setReceiving(Receiving sale) {
        this.receiving = sale;
        initNumbers();
        updateNumbersUI();
        initializeTable();
    }

    private void initNumbers() {
        subtotal = receiving.getTotalAmount() + receiving.getDiscount();
        receivingDiscount = receiving.getDiscount();
        totalAmount = receiving.getTotalAmount();
        amountPaid = receiving.getPaidAmount();
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

        itemsTable.setItems(ItemsRepository.getItemsOfReceiving(receiving));
    }

    private void updateNumbersUI() {
        DecimalFormat formatter = HelperFunctions.getDecimalFormatter();
        subtotalText.setText( formatter.format(subtotal) + " " + receiving.getCurrency());
        discountText.setText(formatter.format(receivingDiscount) + " " + receiving.getCurrency());
        taxesText.setText(formatter.format(taxes) + " " + receiving.getCurrency());
        amountPaidText.setText(formatter.format(amountPaid) + " " + receiving.getCurrency());
        totalText.setText(formatter.format(totalAmount) + " " + receiving.getCurrency());
        remainingAmountLabel.setText(formatter.format(remainingAmount) + " " + receiving.getCurrency());
    }
}