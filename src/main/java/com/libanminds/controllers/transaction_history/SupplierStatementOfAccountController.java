package com.libanminds.controllers.transaction_history;

import com.libanminds.constants.Constants;
import com.libanminds.constants.Views;
import com.libanminds.controllers.dialogs.ViewPurchaseController;
import com.libanminds.models.Purchase;
import com.libanminds.models.Supplier;
import com.libanminds.models.SupplierTransaction;
import com.libanminds.repositories.PurchasesRepository;
import com.libanminds.repositories.TransactionsRepository;
import com.libanminds.singletons.GlobalSettings;
import com.libanminds.utils.HelperFunctions;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SupplierStatementOfAccountController implements Initializable {
    @FXML private Label title;
    @FXML private TableView<SupplierTransaction> transactionsTable;
    @FXML private Label paidText;
    @FXML private Label returnedText;
    @FXML private Label totalText;
    @FXML private ChoiceBox<String> currency;

    private Supplier selectedSupplier;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initChoiceBoxes();
    }

    public void setSelectedCustomer(Supplier supplier) {
        selectedSupplier = supplier;
        title.setText(selectedSupplier.getName() + "'s Statement of Account");
        initializeTable();
        setNumbers(GlobalSettings.fetch().defaultCurrency);
    }

    private void setNumbers(String currency) {
        double paid = 0;
        double returned = 0;
        for (SupplierTransaction transaction : transactionsTable.getItems()) {
            if (transaction.isRefund())
                returned += transaction.getAmount(currency);
            else
                paid += transaction.getAmount(currency);
        }
        paidText.setText(HelperFunctions.getDecimalFormatter().format(paid) + " " + currency);
        returnedText.setText(HelperFunctions.getDecimalFormatter().format(returned) + " " + currency);
        totalText.setText(HelperFunctions.getDecimalFormatter().format(paid - returned) + " " + currency);
    }

    private void initChoiceBoxes() {
        String[] currencies = {Constants.USD_CURRENCY, Constants.LBP_CURRENCY};
        currency.setItems(FXCollections.observableArrayList(currencies));
        currency.setValue(GlobalSettings.fetch().defaultCurrency);
        currency.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldIndex, newIndex) -> setNumbers(currencies[(Integer) newIndex]));
    }

    private void initializeTable() {
        TableColumn<SupplierTransaction, String> invoiceID = new TableColumn<>("Invoice ID");
        TableColumn<SupplierTransaction, String> amountWithCurrency = new TableColumn<>("amount");
        TableColumn<SupplierTransaction, String> isRefund = new TableColumn<>("Is a refund");
        TableColumn<SupplierTransaction, String> transactionDate = new TableColumn<>("Date/Time");

        transactionsTable.getColumns().addAll(invoiceID, amountWithCurrency, isRefund, transactionDate);

        invoiceID.setCellValueFactory(new PropertyValueFactory<>("invoiceID"));
        amountWithCurrency.setCellValueFactory(new PropertyValueFactory<>("amountWithCurrency"));
        isRefund.setCellValueFactory(new PropertyValueFactory<>("isRefund"));
        transactionDate.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

        transactionsTable.setItems(TransactionsRepository.getSuppliersTransactions(selectedSupplier));

        transactionsTable.setRowFactory(tv -> {
            TableRow<SupplierTransaction> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() >= 2 && (!row.isEmpty())) {
                    SupplierTransaction transaction = row.getItem();
                    showViewPurchaseDialog(PurchasesRepository.getPurchase(transaction.getInvoiceID()));
                }
            });
            return row;
        });
    }


    private void showViewPurchaseDialog(Purchase purchase) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.VIEW_PURCHASE));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            ViewPurchaseController controller = loader.getController();
            controller.setPurchase(purchase);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
