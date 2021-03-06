package com.libanminds.controllers.main;

import com.libanminds.constants.AuthorizationKeys;
import com.libanminds.constants.Constants;
import com.libanminds.constants.Views;
import com.libanminds.controllers.dialogs.CompleteSaleController;
import com.libanminds.controllers.dialogs.SelectCarDialogController;
import com.libanminds.controllers.dialogs.SelectCustomerDialogController;
import com.libanminds.controllers.dialogs.SelectItemDialogController;
import com.libanminds.models.Car;
import com.libanminds.models.Customer;
import com.libanminds.models.Item;
import com.libanminds.models.Sale;
import com.libanminds.repositories.SalesRepository;
import com.libanminds.singletons.GlobalSettings;
import com.libanminds.utils.Authorization;
import com.libanminds.utils.EditingCell;
import com.libanminds.utils.HelperFunctions;
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

public class NewSaleController implements Initializable {
    @FXML public Label carName;
    @FXML public Button selectCarBtn;
    @FXML private Label customerName;
    @FXML private Button selectCustomerBtn;
    @FXML private Button selectItemBtn;
    @FXML private Button deleteItem;
    @FXML private Button saveSale;
    @FXML private Button clearSale;
    @FXML private TextField amountPaidField;
    @FXML private TableView<Item> itemsTable;
    @FXML private TableView<Sale> pastInvoicesTable;
    @FXML private TextField saleDiscountTextField;
    @FXML private Label subtotalText;
    @FXML private Label discountText;
    @FXML private Label taxesText;
    @FXML private Label totalText;
    @FXML private CheckBox markAsDiscount;
    @FXML private Label remainingAmountLabel;
    @FXML private ComboBox<String> currencyChoiceBox;
    @FXML private ToggleSwitch isOfficialToggle;

    private Car selectedCar;
    private Customer selectedCustomer;
    // The item returned from the select item window
    private Item selectedItem;
    //The item selected in the sales table
    private Item selectedSaleItem;

    private double subtotal;
    private double salesDiscount;
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
        saleDiscountTextField.setTextFormatter(HelperFunctions.getUnsignedNumberFilter());
        amountPaidField.setTextFormatter(HelperFunctions.getUnsignedNumberFilter());
    }

    private void initButtonsClicks() {
        selectCustomerBtn.setOnMouseClicked((EventHandler<Event>) event -> showSelectCustomerDialog());
        selectCarBtn.setOnMouseClicked((EventHandler<Event>) event -> showSelectCarDialog());
        selectItemBtn.setOnMouseClicked((EventHandler<Event>) event -> showSelectItemDialog());
        saveSale.setOnMouseClicked((EventHandler<Event>) event -> createSale());
        clearSale.setOnMouseClicked((EventHandler<Event>) event -> clearChanges());
        deleteItem.setOnMouseClicked((EventHandler<Event>) event -> showDeleteConfirmationDialog());
        deleteItem.setDisable(selectedSaleItem == null);
        selectCarBtn.setDisable(true);
    }

    //DIALOGS LAUNCHERS BEGINS
    private void showDeleteConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete " + selectedSaleItem.getName() + "?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == yesButton) {
            itemsTable.getItems().remove(selectedSaleItem);
            saleDiscountTextField.setText((salesDiscount = 0) + "");
            amountPaidField.setText((amountPaid = 0) + "");
            calculateSubtotal();
            recalculateNumbers();
        } else {
            alert.close();
        }
    }

    private void showSelectCarDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.SELECT_CAR));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            SelectCarDialogController controller = loader.getController();
            controller.setSaleController(this);
            controller.setOwnerId(selectedCustomer == null ? -1 : selectedCustomer.getID());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSelectCustomerDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.SELECT_CUSTOMER));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            SelectCustomerDialogController controller = loader.getController();
            controller.setSaleController(this);
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
            controller.setHostController(this, null);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //DIALOGS LAUNCHERS ENDS

    private void initChoiceBoxes() {
        String[] currencies = {Constants.USD_CURRENCY, Constants.LBP_CURRENCY};
        currencyChoiceBox.setItems(FXCollections.observableArrayList(currencies));
        currencyChoiceBox.setValue(GlobalSettings.fetch().defaultCurrency);
        currencyChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldIndex, newIndex) -> changeCurrency(currencyChoiceBox.getItems().get((Integer) newIndex)));
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
                new PropertyValueFactory<Item, String>("totalQuantity"));
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
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setDiscount(t.getNewValue().isEmpty() ? 0 : Double.parseDouble(t.getNewValue()));

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
        TableColumn<Sale, String> invoiceTotalAmountCol = new TableColumn<>("Total Amount");
        TableColumn<Sale, String> invoiceBalanceCol = new TableColumn<>("Balance");

        pastInvoicesTable.getColumns().addAll(invoiceTotalAmountCol, invoiceBalanceCol);

        invoiceTotalAmountCol.setCellValueFactory(new PropertyValueFactory<>("formattedTotalAmount"));
        invoiceBalanceCol.setCellValueFactory(new PropertyValueFactory<>("formattedBalance"));

        pastInvoicesTable.setRowFactory(tv -> {
            TableRow<Sale> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() >= 2 && (!row.isEmpty())) {
                    if (Authorization.cannot(AuthorizationKeys.RETURN_SALE_ITEMS)) return;
                    Sale compactSale = row.getItem();
                    showCompleteSaleDialog(SalesRepository.getSale(compactSale.getID()));
                }
            });
            return row;
        });

        //PAST INVOICES TABLE ENDS
    }

    private void showCompleteSaleDialog(Sale sale) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.COMPLETE_SALE));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            CompleteSaleController controller = loader.getController();
            controller.setSale(sale);
            stage.show();
            stage.setOnHidden(e -> pastInvoicesTable.setItems(SalesRepository.getCompactSalesOfCustomer(selectedCustomer.getID())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createSale() {
        if (itemsTable.getItems().isEmpty()) return;

        SalesRepository.createSale(
                itemsTable.getItems(),
                selectedCustomer,
                selectedCar == null ? -1 : selectedCar.getID(),
                markAsDiscount.isSelected() ? remainingAmount + salesDiscount : salesDiscount,
                markAsDiscount.isSelected() ? totalAmount - remainingAmount : totalAmount,
                currencyChoiceBox.getValue(),
                amountPaid,
                !isOfficialToggle.isSelected(),
                "Cash");

        clearChanges();
    }

    private void setTableListener() {
        itemsTable.selectionModelProperty().get().selectedItemProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
            selectedSaleItem = (Item) newValue;
            deleteItem.setDisable(selectedSaleItem == null);
        });
    }

    private void addItemToList() {
        if (selectedItem != null) {
            if (itemsTable.getItems().contains(selectedItem)) {
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

    public void setSelectedCar(Car car) {
        selectedCar = car;
        if (selectedCar != null) {
            carName.setText(selectedCar.getName());
        } else {
            customerName.setText("None");
        }
    }

    public void setSelectedCustomer(Customer customer) {
        selectedCustomer = customer;
        if (selectedCustomer != null) {
            customerName.setText(selectedCustomer.getName());
            pastInvoicesTable.setItems(SalesRepository.getCompactSalesOfCustomer(selectedCustomer.getID()));
        } else {
            customerName.setText("Guest");
            pastInvoicesTable.getItems().clear();
        }
        selectedCar = null;
        carName.setText("None");
        selectCarBtn.setDisable(selectedCustomer == null);
    }

    public void setSelectedItem(Item item) {
        selectedItem = item;
        addItemToList();
    }

    private void setTextFieldsListeners() {
        amountPaidField.textProperty().addListener((observable, oldValue, newValue) -> {
            amountPaid = (newValue.isEmpty() ? 0 : Double.parseDouble(newValue));

            if (amountPaid > totalAmount) {
                amountPaidField.setText(oldValue);
            }
            recalculateNumbers();
        });

        saleDiscountTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            salesDiscount = (newValue.isEmpty() ? 0 : Double.parseDouble(newValue));

            if (salesDiscount > (subtotal - amountPaid)) {
                saleDiscountTextField.setText(oldValue);
            } else
                recalculateNumbers();
        });
    }

    private void clearChanges() {
        subtotal = 0;
        salesDiscount = 0;
        taxes = 0;
        amountPaid = 0;
        totalAmount = 0;
        remainingAmount = 0;

        selectedCustomer = null;
        selectedCar = null;

        selectCarBtn.setDisable(true);
        customerName.setText("Guest");
        carName.setText("None");
        itemsTable.getItems().clear();
        pastInvoicesTable.getItems().clear();
        amountPaidField.setText("");
        markAsDiscount.setSelected(false);
        saleDiscountTextField.setText("");
        isOfficialToggle.setSelected(false);
        updateNumbersUI();
    }

    private void changeCurrency(String currency) {
        for (int i = 0; i < itemsTable.getItems().size(); i++)
            itemsTable.getItems().get(i).setSaleCurrency(currency);

        calculateSubtotal();
        recalculateNumbers();
    }

    private void calculateSubtotal() {
        subtotal = 0;
        for (int i = 0; i < itemsTable.getItems().size(); i++)
            subtotal += itemsTable.getItems().get(i).getTotal();
    }

    private void recalculateNumbers() {
        System.out.println(salesDiscount);
        totalAmount = subtotal - salesDiscount - taxes;
        remainingAmount = totalAmount - amountPaid;

        updateNumbersUI();
    }

    private void updateNumbersUI() {
        DecimalFormat formatter = HelperFunctions.getDecimalFormatter();
        subtotalText.setText(formatter.format(subtotal) + " " + currencyChoiceBox.getValue());
        discountText.setText(formatter.format(salesDiscount) + " " + currencyChoiceBox.getValue());
        taxesText.setText(formatter.format(taxes) + " " + currencyChoiceBox.getValue());
        totalText.setText(formatter.format(totalAmount) + " " + currencyChoiceBox.getValue());
        remainingAmountLabel.setText(formatter.format(remainingAmount) + " " + currencyChoiceBox.getValue());
    }
}