package com.libanminds.controllers.dialogs;

import com.libanminds.models.Item;
import com.libanminds.models.Purchase;
import com.libanminds.repositories.ItemsRepository;
import com.libanminds.repositories.PurchasesRepository;
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

public class ReturnPurchaseItemsController implements Initializable {
    @FXML private TableView<Item> purchasedItemsTable;
    @FXML private TableView<Item> returnedItemsTable;
    @FXML private Label subtotalText;
    @FXML private Label discountCurrencyText;
    @FXML private Label taxesText;
    @FXML private Label totalText;
    @FXML private Label amountPaidText;
    @FXML private Label refundAmountText;
    @FXML private Label remainingAmountLabel;
    @FXML private TextField discountField;
    @FXML private Button savePurchase;

    private Purchase purchase;

    private double subtotal;
    private double purchaseDiscount;
    private double taxes;
    private double totalAmount;
    private double amountPaid;
    private double amountToRefund;
    private double remainingAmount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtonsClicks();
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
        initNumbers();
        updateNumbersUI();
        initializeTables();
        initFilters();
        setTextFieldsListeners();
    }

    private void initFilters() {
        discountField.setTextFormatter(HelperFunctions.getUnsignedNumberFilter());
    }

    private void initButtonsClicks() {
        savePurchase.setOnMouseClicked((EventHandler<Event>) event -> SaveChanges());
    }

    private void initializeTables() {

        //PURCHASED ITEMS TABLE START
        TableColumn<Item, String> codeCol = new TableColumn<>("Code");
        TableColumn<Item, String> nameCol = new TableColumn<>("Name");
        TableColumn<Item, String> saleQuantityCol = new TableColumn<>("Quantity");
        TableColumn<Item, String> priceCol = new TableColumn<>("Price");
        TableColumn<Item, String> saleDiscountCol = new TableColumn<>("Discount");
        TableColumn<Item, String> totalPriceCol = new TableColumn<>("Total");

        purchasedItemsTable.getColumns().addAll(codeCol, nameCol, saleQuantityCol, priceCol, saleDiscountCol, totalPriceCol);

        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        saleQuantityCol.setCellValueFactory(new PropertyValueFactory<>("saleQuantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("formattedPrice"));
        saleDiscountCol.setCellValueFactory(new PropertyValueFactory<>("formattedDiscount"));
        totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("formattedTotal"));


        purchasedItemsTable.setRowFactory(tv -> {
            TableRow<Item> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() >= 2 && (!row.isEmpty())) {
                    Item item = row.getItem();
                    if (item.getSaleQuantityValue() <= 0)
                        return;
                    if (returnedItemsTable.getItems().contains(item)) {
                        item.incrementReturnedQuantity();
                    } else {
                        item.incrementReturnedQuantity();
                        purchasedItemsTable.refresh();
                        returnedItemsTable.getItems().add(item);
                    }
                    returnedItemsTable.refresh();
                    purchasedItemsTable.refresh();
                    recalculateNumbers();
                }
            });
            return row;
        });

        purchasedItemsTable.setItems(ItemsRepository.getItemsOfPurchase(purchase));

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
                    int newValue = Integer.parseInt(t.getNewValue().isEmpty() ? "0" : t.getNewValue());
                    Item item = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());//

                    if (newValue > item.getInitiallyAvailableQuantity()) {
                        return;
                    }

                    item.setReturnedQuantity(newValue);
                    returnedItemsTable.refresh();
                    purchasedItemsTable.refresh();
                    recalculateNumbers();
                }
        );

        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    private void SaveChanges() {
        purchase.setDiscount(purchaseDiscount);
        purchase.setPaidAmount(amountPaid - amountToRefund);
        purchase.setTotalAmount(totalAmount);
        PurchasesRepository.returnPurchasedItems(purchase, returnedItemsTable.getItems(), amountToRefund);
        Stage currentStage = (Stage) savePurchase.getScene().getWindow();
        currentStage.close();
    }

    private void initNumbers() {
        subtotal = purchase.getTotalAmount() + purchase.getDiscount();
        purchaseDiscount = purchase.getDiscount();
        totalAmount = purchase.getTotalAmount();
        amountPaid = purchase.getPaidAmount();
        amountToRefund = 0;
        remainingAmount = totalAmount - amountPaid;
        discountField.setText(purchaseDiscount + "");
    }

    private void setTextFieldsListeners() {
        discountField.textProperty().addListener((observable, oldValue, newValue) -> {
            purchaseDiscount = (newValue.isEmpty() ? 0 : Double.parseDouble(newValue));

            if (purchaseDiscount > subtotal) {
                discountField.setText(oldValue);
            }

            recalculateNumbers();
        });
    }

    private void recalculateNumbers() {

        subtotal = 0;
        for (int i = 0; i < purchasedItemsTable.getItems().size(); i++) {
            subtotal += purchasedItemsTable.getItems().get(i).getTotal();
        }

        if (purchaseDiscount > subtotal) {
            purchaseDiscount = subtotal;
            discountField.setText(purchaseDiscount + "");
        }

        totalAmount = subtotal - purchaseDiscount + taxes;
        remainingAmount = totalAmount - amountPaid;

        if (remainingAmount < 0) {
            amountToRefund = Math.abs(remainingAmount);
            remainingAmount = 0;
        } else {
            amountToRefund = 0;
        }

        updateNumbersUI();
    }

    private void updateNumbersUI() {
        DecimalFormat formatter = HelperFunctions.getDecimalFormatter();
        subtotalText.setText(formatter.format(subtotal) + " " + purchase.getCurrency());
        discountCurrencyText.setText(purchase.getCurrency());
        taxesText.setText(formatter.format(taxes) + " " + purchase.getCurrency());
        totalText.setText(formatter.format(totalAmount) + " " + purchase.getCurrency());
        amountPaidText.setText(formatter.format(amountPaid) + " " + purchase.getCurrency());
        refundAmountText.setText(formatter.format(amountToRefund) + " " + purchase.getCurrency());
        remainingAmountLabel.setText(formatter.format(remainingAmount) + " " + purchase.getCurrency());
    }
}