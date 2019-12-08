package com.libanminds.main_controllers;

import com.jfoenix.controls.JFXButton;
import com.libanminds.dialogs_controllers.SelectItemDialogController;
import com.libanminds.dialogs_controllers.SelectSupplierDialogController;
import com.libanminds.models.Item;
import com.libanminds.models.Receiving;
import com.libanminds.models.Supplier;
import com.libanminds.repositories.ReceivingsRepository;
import com.libanminds.utils.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.ToggleSwitch;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.ResourceBundle;

public class NewReceivingController implements Initializable {


    @FXML
    private Label supplierName;

    @FXML
    private Button selectSupplierBtn;

    @FXML
    private ComboBox<String> currencyChoiceBox;

    @FXML
    private ToggleSwitch isOfficialToggle;

    @FXML
    private Button addItemBtn;

    @FXML
    private Button deleteItemBtn;

    @FXML
    private TableView<Item> itemsTable;

    @FXML
    private TextField receivingsDiscountField;

    @FXML
    private Label subtotalText;

    @FXML
    private Label discountText;

    @FXML
    private Label taxesText;

    @FXML
    private Label totalText;

    @FXML
    private CheckBox markAsDiscount;

    @FXML
    private TextField amountPaidField;

    @FXML
    private Label remainingAmountText;

    @FXML
    private JFXButton saveReceiving;

    @FXML
    private TableView<Receiving> pastInvoicesTable;


    private Supplier selectedSupplier;
    // The item returned from the select item window
    private Item selectedItem;
    //The item selected in the Receiving table
    private Item selectedReceivingItem;

    private double subtotal;
    private double receivingDiscount;
    private double taxes;
    private double amountPaid;
    private double totalAmount;
    private double remainingAmount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtonsClicks();
        initializeTables();
        initChoiceBoxes();
        setTableListener();
        setTextFieldsListeners();
        initFilters();
    }

    private void initFilters() {
        receivingsDiscountField.setTextFormatter(HelperFunctions.getUnsignedNumberFilter());
        amountPaidField.setTextFormatter(HelperFunctions.getUnsignedNumberFilter());
    }

    private void initButtonsClicks() {
        selectSupplierBtn.setOnMouseClicked((EventHandler<Event>) event -> showSelectSupplierDialog());
        addItemBtn.setOnMouseClicked((EventHandler<Event>) event -> showSelectItemDialog());
        saveReceiving.setOnMouseClicked((EventHandler<Event>) event -> createReceiving());
        deleteItemBtn.setOnMouseClicked((EventHandler<Event>) event -> showDeleteConfirmationDialog());
        deleteItemBtn.setDisable(selectedReceivingItem == null);
    }

    private void showSelectSupplierDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.SELECT_SUPPLIER));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            SelectSupplierDialogController controller = loader.getController();
            controller.setHostController(this);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSelectItemDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.SELECT_ITEM));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            SelectItemDialogController controller = loader.getController();
            controller.setHostController(null,this);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDeleteConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete " + selectedReceivingItem.getName() + "?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == yesButton) {
            itemsTable.getItems().remove(selectedReceivingItem);
            receivingsDiscountField.setText((receivingDiscount = 0) + "");
            amountPaidField.setText((amountPaid = 0) + "");
            calculateSubtotal();
            recalculateNumbers();
        } else {
            alert.close();
        }
    }

    private void createReceiving() {
        ReceivingsRepository.createReceiving(
                itemsTable.getItems(),
                selectedSupplier,
                markAsDiscount.isSelected() ? remainingAmount + receivingDiscount : receivingDiscount,
                markAsDiscount.isSelected() ? totalAmount - remainingAmount : totalAmount,
                currencyChoiceBox.getValue(),
                amountPaid,
                !isOfficialToggle.isSelected(),
                "Cash");

        clearChanges();
    }

    private void clearChanges() {
        subtotal = 0;
        receivingDiscount = 0;
        taxes = 0;
        amountPaid = 0;
        totalAmount = 0;
        remainingAmount = 0;
        selectedSupplier = null;
        supplierName.setText("");
        itemsTable.getItems().clear();
        amountPaidField.setText("");
        markAsDiscount.setSelected(false);
        receivingsDiscountField.setText("");
        isOfficialToggle.setSelected(false);
        updateNumbersUI();
    }

    public void setSelectedSupplier(Supplier supplier) {
        selectedSupplier = supplier;
        if(selectedSupplier != null) {

            supplierName.setText(selectedSupplier.getName());
            pastInvoicesTable.setItems(ReceivingsRepository.getCompactReceivingOfCustomer(selectedSupplier.getID()));
        }
        else {
            supplierName.setText("Guest");
            pastInvoicesTable.getItems().clear();
        }
    }

    private void initChoiceBoxes() {
        String[] currencies = {Constants.DOLLAR_CURRENCY, Constants.LIRA_CURRENCY};
        currencyChoiceBox.setItems(FXCollections.observableArrayList(currencies));
        currencyChoiceBox.setValue(GlobalSettings.DEFAULT_CURRENCY);
        currencyChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldIndex, newIndex) -> changeCurrency(currencyChoiceBox.getItems().get((Integer) newIndex)));
    }

    private void changeCurrency(String currency) {
        for (int i = 0; i < itemsTable.getItems().size(); i++)
            itemsTable.getItems().get(i).setSaleCurrency(currency);

        calculateSubtotal();
        recalculateNumbers();
    }

    private void initializeTables() {
        //ITEMS TABLE STARTS
        itemsTable.setEditable(true);
        TableColumn<Item, String> codeCol = new TableColumn<>("Code");
        TableColumn<Item, String> nameCol = new TableColumn<>("Name");
        TableColumn saleQuantityCol = new TableColumn<>("Quantity");
        TableColumn<Item, String> priceCol = new TableColumn<>("Price");
        TableColumn saleDiscountCol = new TableColumn<>("Discount");
        TableColumn<Item, String> totalPriceCol = new TableColumn<>("Total");

        itemsTable.getColumns().addAll(codeCol, nameCol, saleQuantityCol, priceCol, saleDiscountCol, totalPriceCol);

        Callback<TableColumn, TableCell> quantityCellFactory =
                p -> new EditingCell(true);

        Callback<TableColumn, TableCell> discountCellFactory =
                p -> new EditingCell(false);

        saleQuantityCol.setCellValueFactory(
                new PropertyValueFactory<Item, String>("saleQuantity"));
        saleQuantityCol.setCellFactory(quantityCellFactory);

        saleQuantityCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Item, String>>) t -> {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setTotalQuantity(Integer.parseInt(t.getNewValue().isEmpty() ? "1" : t.getNewValue()));

                    calculateSubtotal();
                    recalculateNumbers();
                }
        );

        saleDiscountCol.setCellValueFactory(
                new PropertyValueFactory<Item, String>("discount"));
        saleDiscountCol.setCellFactory(discountCellFactory);

        saleDiscountCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Item, String>>) t -> {
                    ((Item) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setDiscount(t.getNewValue().isEmpty() ? 0 : Double.parseDouble(t.getNewValue()));

                    calculateSubtotal();
                    recalculateNumbers();
                }
        );

        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(cellData -> cellData.getValue().getFormattedPriceProperty());
        totalPriceCol.setCellValueFactory(cellData -> cellData.getValue().getFormattedTotalProperty());
        //ITEMS TABLE ENDS

        //PAST INVOICES TABLE STARTS
        TableColumn<Receiving, String> invoiceTotalAmountCol = new TableColumn<>("Total Amount");
        TableColumn<Receiving, String> invoiceBalanceCol = new TableColumn<>("Balance");

        pastInvoicesTable.getColumns().addAll(invoiceTotalAmountCol, invoiceBalanceCol);

        invoiceTotalAmountCol.setCellValueFactory(new PropertyValueFactory<>("formattedTotalAmount"));
        invoiceBalanceCol.setCellValueFactory(new PropertyValueFactory<>("formattedBalance"));
        //PAST INVOICES TABLE ENDS
    }

    private void setTableListener() {
        itemsTable.selectionModelProperty().get().selectedItemProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
            selectedReceivingItem = (Item) newValue;
            deleteItemBtn.setDisable(selectedReceivingItem == null);
        });
    }

    private void setTextFieldsListeners() {
        amountPaidField.textProperty().addListener((observable, oldValue, newValue) -> {
            amountPaid = (newValue.isEmpty() ? 0 : Double.parseDouble(newValue));

            if (amountPaid > totalAmount) {
                amountPaidField.setText(oldValue);
            }
            recalculateNumbers();
        });

        receivingsDiscountField.textProperty().addListener((observable, oldValue, newValue) -> {
            receivingDiscount = (newValue.isEmpty() ? 0 : Double.parseDouble(newValue));

            if (receivingDiscount > (subtotal - amountPaid)) {
                receivingsDiscountField.setText(oldValue);
            }
            recalculateNumbers();
        });
    }

    public void setSelectedItem(Item item) {
        selectedItem = item;
        addItemToList();
    }

    private void addItemToList() {
        if (selectedItem != null) {
            if(itemsTable.getItems().contains(selectedItem)) {
                itemsTable.getItems().get(itemsTable.getItems().indexOf(selectedItem)).incrementTotalQuantity();
                itemsTable.refresh();
            } else {
                selectedItem.setSaleCurrency(currencyChoiceBox.getValue());
                itemsTable.getItems().add(selectedItem);
            }
            calculateSubtotal();
            recalculateNumbers();
            setSelectedItem(null);
        }
    }

    private void calculateSubtotal() {
        subtotal = 0;
        for (int i = 0; i < itemsTable.getItems().size(); i++)
            subtotal += itemsTable.getItems().get(i).getTotal();
    }

    private void recalculateNumbers() {
        totalAmount = subtotal - receivingDiscount - taxes;
        remainingAmount = totalAmount - amountPaid;

        updateNumbersUI();
    }

    private void updateNumbersUI() {
        DecimalFormat formatter = HelperFunctions.getDecimalFormatter();
        subtotalText.setText( formatter.format(subtotal) + " " + currencyChoiceBox.getValue());
        discountText.setText(formatter.format(receivingDiscount) + " " + currencyChoiceBox.getValue());
        taxesText.setText(formatter.format(taxes) + " " + currencyChoiceBox.getValue());
        totalText.setText(formatter.format(totalAmount) + " " + currencyChoiceBox.getValue());
        remainingAmountText.setText(formatter.format(remainingAmount) + " " + currencyChoiceBox.getValue());
    }
}
