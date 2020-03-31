package com.libanminds.controllers.dialogs;

import com.libanminds.models.CustomerTransaction;
import com.libanminds.models.Item;
import com.libanminds.models.Sale;
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

public class ViewSaleController implements Initializable {
    @FXML private TableView<Item> itemsTable;
    @FXML private TableView<CustomerTransaction> transactionsTable;
    @FXML private TableView<Item> returnedItemsTable;
    @FXML private Label subtotalText;
    @FXML private Label discountText;
    @FXML private Label taxesText;
    @FXML private Label totalText;
    @FXML private Label amountPaidText;
    @FXML private Label remainingAmountLabel;
    @FXML private Label customerName;

    private double subtotal;
    private double salesDiscount;
    private double taxes;
    private double totalAmount;
    private double amountPaid;
    private double remainingAmount;

    private Sale sale;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setSale(Sale sale) {
        this.sale = sale;
        initNumbers();
        updateNumbersUI();
        initializeTable();
        initLabels(this.sale.getCustomerName());
    }

    private void initNumbers() {
        subtotal = sale.getTotalAmount() + sale.getDiscount();
        salesDiscount = sale.getDiscount();
        totalAmount = sale.getTotalAmount();
        amountPaid = sale.getPaidAmount();
        remainingAmount = totalAmount - amountPaid;
    }

    private void initLabels(String name) {
        customerName.setText("Customer: " + name);
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


        TableColumn<CustomerTransaction, String> transactionAmount = new TableColumn<>("Amount");
        TableColumn<CustomerTransaction, String> transactionIsRefund = new TableColumn<>("is Refund");
        TableColumn<CustomerTransaction, String> transactionDate = new TableColumn<>("Date/Time");

        transactionsTable.getColumns().addAll(transactionAmount, transactionIsRefund, transactionDate);

        transactionAmount.setCellValueFactory(new PropertyValueFactory<>("amountWithCurrency"));
        transactionIsRefund.setCellValueFactory(new PropertyValueFactory<>("isRefund"));
        transactionDate.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

        transactionsTable.setItems(TransactionsRepository.getSaleTransactions(sale));


        TableColumn<Item, String> itemCode = new TableColumn<>("Code");
        TableColumn<Item, String> itemName = new TableColumn<>("Name");
        TableColumn<Item, String> returnedQuantity = new TableColumn<>("Quantity");

        returnedItemsTable.getColumns().addAll(itemCode, itemName, returnedQuantity);

        itemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        itemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        returnedQuantity.setCellValueFactory(new PropertyValueFactory<>("previouslyReturnedQuantity"));

        returnedItemsTable.setItems(ItemsRepository.getReturnedItemsOfSale(sale));
    }

    private void updateNumbersUI() {
        DecimalFormat formatter = HelperFunctions.getDecimalFormatter();
        subtotalText.setText(formatter.format(subtotal) + " " + sale.getCurrency());
        discountText.setText(formatter.format(salesDiscount) + " " + sale.getCurrency());
        taxesText.setText(formatter.format(taxes) + " " + sale.getCurrency());
        amountPaidText.setText(formatter.format(amountPaid) + " " + sale.getCurrency());
        totalText.setText(formatter.format(totalAmount) + " " + sale.getCurrency());
        remainingAmountLabel.setText(formatter.format(remainingAmount) + " " + sale.getCurrency());
    }
}