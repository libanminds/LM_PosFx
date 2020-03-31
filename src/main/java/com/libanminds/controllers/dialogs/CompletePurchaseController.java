package com.libanminds.controllers.dialogs;

import com.libanminds.models.Item;
import com.libanminds.models.Purchase;
import com.libanminds.repositories.ItemsRepository;
import com.libanminds.repositories.PurchasesRepository;
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

public class CompletePurchaseController implements Initializable {
    @FXML private TableView<Item> itemsTable;
    @FXML private TextField purchaseDiscountTextField;
    @FXML private CheckBox markAsDiscount;
    @FXML private Label subtotalText;
    @FXML private Label discountText;
    @FXML private Label taxesText;
    @FXML private Label totalText;
    @FXML private Label amountPaidText;
    @FXML private TextField newPaymentField;
    @FXML private Label remainingAmountLabel;
    @FXML private Button saveBtn;

    private double subtotal;
    private double purchaseDiscount;
    private double newPurchaseDiscount;
    private double taxes;
    private double totalAmount;
    private double amountPaid;
    private double newPayment;
    private double remainingAmount;

    private Purchase purchase;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtonsClicks();
        setTextFieldsListeners();
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
        initNumbers();
        updateNumbersUI();
        initializeTable();
        initFilters();
    }

    private void initNumbers() {
        subtotal = purchase.getTotalAmount() + purchase.getDiscount();
        purchaseDiscount = purchase.getDiscount();
        totalAmount = purchase.getTotalAmount();
        amountPaid = purchase.getPaidAmount();
        remainingAmount = totalAmount - amountPaid;
    }

    private void initFilters() {
        purchaseDiscountTextField.setTextFormatter(HelperFunctions.getUnsignedNumberFilter());
        newPaymentField.setTextFormatter(HelperFunctions.getUnsignedNumberFilter());
    }

    private void initButtonsClicks() {
        saveBtn.setOnMouseClicked((EventHandler<Event>) event -> updatePurchase());
    }

    private void updatePurchase() {
        PurchasesRepository.completePurchasePayment(
                purchase.getID(),
                purchase.getSupplierID(),
                markAsDiscount.isSelected() ? purchaseDiscount + newPurchaseDiscount + remainingAmount : purchaseDiscount + newPurchaseDiscount,
                markAsDiscount.isSelected() ? totalAmount - remainingAmount : totalAmount,
                amountPaid + newPayment,
                newPayment,
                purchase.getCurrency()
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

        itemsTable.setItems(ItemsRepository.getItemsOfPurchase(purchase));
    }

    private void setTextFieldsListeners() {
        newPaymentField.textProperty().addListener((observable, oldValue, newValue) -> {
            newPayment = (newValue.isEmpty() ? 0 : Double.parseDouble(newValue));

            if ((amountPaid + newPayment) > totalAmount) {
                newPaymentField.setText(oldValue);
            }

            recalculateNumbers();
        });

        purchaseDiscountTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            newPurchaseDiscount = (newValue.isEmpty() ? 0 : Double.parseDouble(newValue));

            if ((purchaseDiscount + newPurchaseDiscount) > (subtotal - (amountPaid + newPayment))) {
                purchaseDiscountTextField.setText(oldValue);
            }

            recalculateNumbers();
        });
    }

    private void recalculateNumbers() {
        totalAmount = subtotal - purchaseDiscount + taxes - newPurchaseDiscount;
        remainingAmount = totalAmount - amountPaid - newPayment;

        updateNumbersUI();
    }

    private void updateNumbersUI() {
        DecimalFormat formatter = HelperFunctions.getDecimalFormatter();
        subtotalText.setText(formatter.format(subtotal) + " " + purchase.getCurrency());
        discountText.setText(formatter.format(purchaseDiscount + newPurchaseDiscount) + " " + purchase.getCurrency());
        taxesText.setText(formatter.format(taxes) + " " + purchase.getCurrency());
        amountPaidText.setText(formatter.format(amountPaid) + " " + purchase.getCurrency());
        totalText.setText(formatter.format(totalAmount) + " " + purchase.getCurrency());
        remainingAmountLabel.setText(formatter.format(remainingAmount) + " " + purchase.getCurrency());
    }
}