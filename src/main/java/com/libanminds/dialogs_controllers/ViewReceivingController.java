package com.libanminds.dialogs_controllers;

import com.libanminds.models.Item;
import com.libanminds.models.Receiving;
import com.libanminds.models.SupplierTransaction;
import com.libanminds.repositories.ItemsRepository;
import com.libanminds.repositories.TransactionsRepository;
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
    private TableView<SupplierTransaction> transactionsTable;

    @FXML
    private TableView<Item> returnedItemsTable;

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

    @FXML
    private Label supplierName;

    private double subtotal;
    private double receivingDiscount;
    private double taxes;
    private double totalAmount;
    private double amountPaid;
    private double remainingAmount;

    private Receiving receiving;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void setReceiving(Receiving receiving) {
        this.receiving = receiving;
        initNumbers();
        updateNumbersUI();
        initializeTable();
        initLabels(receiving.getSupplierName());
    }

    private void initNumbers() {
        subtotal = receiving.getTotalAmount() + receiving.getDiscount();
        receivingDiscount = receiving.getDiscount();
        totalAmount = receiving.getTotalAmount();
        amountPaid = receiving.getPaidAmount();
        remainingAmount = totalAmount - amountPaid;
    }

    private void initLabels(String name) {
        supplierName.setText("Supplier: " + name);
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



        TableColumn<SupplierTransaction, String> transactionAmount = new TableColumn<>("Amount");
        TableColumn<SupplierTransaction, String> transactionIsRefund = new TableColumn<>("is Refund");
        TableColumn<SupplierTransaction, String> transactionDate = new TableColumn<>("Date/Time");

        transactionsTable.getColumns().addAll(transactionAmount, transactionIsRefund, transactionDate);

        transactionAmount.setCellValueFactory(new PropertyValueFactory<>("amountWithCurrency"));
        transactionIsRefund.setCellValueFactory(new PropertyValueFactory<>("isRefund"));
        transactionDate.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

        transactionsTable.setItems(TransactionsRepository.getReceivingTransactions(receiving));

        TableColumn<Item, String> itemCode = new TableColumn<>("Code");
        TableColumn<Item, String> itemName = new TableColumn<>("Name");
        TableColumn<Item, String> returnedQuantity = new TableColumn<>("Quantity");

        returnedItemsTable.getColumns().addAll(itemCode, itemName, returnedQuantity);

        itemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        itemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        returnedQuantity.setCellValueFactory(new PropertyValueFactory<>("previouslyReturnedQuantity"));

        returnedItemsTable.setItems(ItemsRepository.getReturnedItemsOfReceiving(receiving));
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