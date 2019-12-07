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

public class CompleteSaleController implements Initializable {

    @FXML
    private TableView<Item> itemsTable;

    @FXML
    private TextField saleDiscountTextField;

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
    private JFXButton saveSale;

    private double subtotal;
    private double salesDiscount;
    private double newSalesDiscount;
    private double taxes;
    private double totalAmount;
    private double amountPaid;
    private double newPayment;
    private double remainingAmount;

    private Sale sale;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtonsClicks();
        setTextFieldsListeners();
    }

    public void setSale(Sale sale) {
        this.sale = sale;
        initNumbers();
        updateNumbersUI();
        initializeTable();
        initFilters();
    }

    private void initNumbers() {
        subtotal = sale.getTotalAmount() + sale.getDiscount();
        salesDiscount = sale.getDiscount();
        totalAmount = sale.getTotalAmount();
        amountPaid = sale.getPaidAmount();
        remainingAmount = totalAmount - amountPaid;
    }

    private void initFilters() {
        saleDiscountTextField.setTextFormatter(HelperFunctions.getUnsignedNumberFilter());
        newPaymentField.setTextFormatter(HelperFunctions.getUnsignedNumberFilter());
    }

    private void initButtonsClicks() {
        saveSale.setOnMouseClicked((EventHandler<Event>) event -> updateSale());
    }

    private void updateSale() {
        SalesRepository.completeSalePayment(
                sale.getID(),
                markAsDiscount.isSelected()? salesDiscount + newSalesDiscount + remainingAmount : salesDiscount + newSalesDiscount,
                markAsDiscount.isSelected() ? totalAmount - remainingAmount : totalAmount,
                amountPaid + newPayment
        );

        Stage currentStage = (Stage) saveSale.getScene().getWindow();
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

        itemsTable.setItems(ItemsRepository.getItemsOfSale(sale));
    }

    private void setTextFieldsListeners() {
        newPaymentField.textProperty().addListener((observable, oldValue, newValue) -> {
            newPayment = (newValue.isEmpty() ? 0 : Double.parseDouble(newValue));
            recalculateNumbers();
        });

        saleDiscountTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            newSalesDiscount = (newValue.isEmpty() ? 0 : Double.parseDouble(newValue));
            recalculateNumbers();
        });
    }

    private void recalculateNumbers() {
        totalAmount = subtotal - salesDiscount + taxes - newSalesDiscount;
        remainingAmount = totalAmount - amountPaid - newPayment;

        updateNumbersUI();
    }

    private void updateNumbersUI() {
        DecimalFormat formatter = HelperFunctions.getDecimalFormatter();
        subtotalText.setText( formatter.format(subtotal) + " " + sale.getCurrency());
        discountText.setText(formatter.format(salesDiscount + newSalesDiscount) + " " + sale.getCurrency());
        taxesText.setText(formatter.format(taxes) + " " + sale.getCurrency());
        amountPaidText.setText(formatter.format(amountPaid) + " " + sale.getCurrency());
        totalText.setText(formatter.format(totalAmount) + " " + sale.getCurrency());
        remainingAmountLabel.setText(formatter.format(remainingAmount) + " " + sale.getCurrency());
    }
}