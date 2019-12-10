package com.libanminds.transactions_history_controllers;

import com.libanminds.models.Customer;
import com.libanminds.models.CustomerTransaction;
import com.libanminds.models.Supplier;
import com.libanminds.repositories.TransactionsRepository;
import com.libanminds.utils.Constants;
import com.libanminds.utils.GlobalSettings;
import com.libanminds.utils.HelperFunctions;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class SupplierStatementOfAccountController implements Initializable {

    @FXML
    private Label title;

    @FXML
    private TableView<CustomerTransaction> transactionsTable;

    @FXML
    private Label paidText;

    @FXML
    private Label returnedText;

    @FXML
    private Label totalText;

    @FXML
    private ChoiceBox<String> currency;


    private Supplier selectedSupplier;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initChoiceBoxes();
    }

    public void setSelectedCustomer(Supplier supplier) {
        selectedSupplier = supplier;
        title.setText(selectedSupplier.getName() + "'s Statement of Account");
        initializeTable();
        setNumbers(GlobalSettings.DEFAULT_CURRENCY);
    }

    private void setNumbers(String currency) {
        double paid = 0;
        double returned = 0;
        for (CustomerTransaction transaction : transactionsTable.getItems()) {
            if(transaction.isRefund())
                returned += transaction.getAmount(currency);
            else
                paid += transaction.getAmount(currency);
        }
        paidText.setText(HelperFunctions.getDecimalFormatter().format(paid) + " " + currency);
        returnedText.setText(HelperFunctions.getDecimalFormatter().format(returned) + " " + currency);
        totalText.setText(HelperFunctions.getDecimalFormatter().format(paid - returned) + " " + currency);
    }

    private void initChoiceBoxes() {
        String[] currencies = { Constants.DOLLAR_CURRENCY, Constants.LIRA_CURRENCY};
        currency.setItems(FXCollections.observableArrayList(currencies));
        currency.setValue(GlobalSettings.DEFAULT_CURRENCY);
        currency.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldIndex, newIndex) -> setNumbers(currencies[(Integer)newIndex]));
    }

    private void initializeTable() {
        TableColumn<CustomerTransaction,String> invoiceID = new TableColumn<>("Invoice ID");
        TableColumn<CustomerTransaction,String> amountWithCurrency = new TableColumn<>("amount");
        TableColumn<CustomerTransaction,String> isRefund = new TableColumn<>("Is a refund");
        TableColumn<CustomerTransaction,String> transactionDate = new TableColumn<>("Date/Time");

        transactionsTable.getColumns().addAll(invoiceID,amountWithCurrency,isRefund,transactionDate);

        invoiceID.setCellValueFactory(new PropertyValueFactory<>("invoiceID"));
        amountWithCurrency.setCellValueFactory(new PropertyValueFactory<>("amountWithCurrency"));
        isRefund.setCellValueFactory(new PropertyValueFactory<>("isRefund"));
        transactionDate.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

        transactionsTable.setItems(TransactionsRepository.getSuppliersTransactions(selectedSupplier));
    }
}
