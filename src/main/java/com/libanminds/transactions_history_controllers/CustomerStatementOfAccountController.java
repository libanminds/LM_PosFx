package com.libanminds.transactions_history_controllers;

import com.libanminds.dialogs_controllers.CustomerDialogController;
import com.libanminds.main_controllers.NewSaleController;
import com.libanminds.models.Customer;
import com.libanminds.models.CustomerTransaction;
import com.libanminds.models.ItemCategory;
import com.libanminds.repositories.CustomersRepository;
import com.libanminds.repositories.ItemsCategoriesRepository;
import com.libanminds.repositories.TransactionsRepository;
import com.libanminds.utils.Constants;
import com.libanminds.utils.GlobalSettings;
import com.libanminds.utils.HelperFunctions;
import com.libanminds.utils.Views;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerStatementOfAccountController implements Initializable {

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

    private Customer selectedCustomer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initChoiceBoxes();
    }

    public void setSelectedCustomer(Customer customer) {
        selectedCustomer = customer;
        title.setText(selectedCustomer.getName() + "'s Statement of Account");
        initializeTable();
        setNumbers(GlobalSettings.DEFAULT_CURRENCY);
    }

    private void initChoiceBoxes() {
        String[] currencies = { Constants.DOLLAR_CURRENCY, Constants.LIRA_CURRENCY};
        currency.setItems(FXCollections.observableArrayList(currencies));
        currency.setValue(GlobalSettings.DEFAULT_CURRENCY);
        currency.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldIndex, newIndex) -> setNumbers(currencies[(Integer)newIndex]));
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

        transactionsTable.setItems(TransactionsRepository.getCustomerTransactions(selectedCustomer));
    }
}
