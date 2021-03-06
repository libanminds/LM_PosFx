package com.libanminds.controllers.transaction_history;

import com.libanminds.constants.Constants;
import com.libanminds.constants.Views;
import com.libanminds.controllers.dialogs.ViewSaleController;
import com.libanminds.models.Customer;
import com.libanminds.models.CustomerTransaction;
import com.libanminds.models.Sale;
import com.libanminds.repositories.SalesRepository;
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

public class CustomerStatementOfAccountController implements Initializable {
    @FXML private Label title;
    @FXML private TableView<CustomerTransaction> transactionsTable;
    @FXML private Label paidText;
    @FXML private Label returnedText;
    @FXML private Label totalText;
    @FXML private ChoiceBox<String> currency;

    private Customer selectedCustomer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initChoiceBoxes();
    }

    public void setSelectedCustomer(Customer customer) {
        selectedCustomer = customer;
        title.setText(selectedCustomer.getName() + "'s Statement of Account");
        initializeTable();
        setNumbers(GlobalSettings.fetch().defaultCurrency);
    }

    private void initChoiceBoxes() {
        String[] currencies = {Constants.USD_CURRENCY, Constants.LBP_CURRENCY};
        currency.setItems(FXCollections.observableArrayList(currencies));
        currency.setValue(GlobalSettings.fetch().defaultCurrency);
        currency.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldIndex, newIndex) -> setNumbers(currencies[(Integer) newIndex]));
    }

    private void setNumbers(String currency) {
        double paid = 0;
        double returned = 0;
        for (CustomerTransaction transaction : transactionsTable.getItems()) {
            if (transaction.isRefund())
                returned += transaction.getAmount(currency);
            else
                paid += transaction.getAmount(currency);
        }
        paidText.setText(HelperFunctions.getDecimalFormatter().format(paid) + " " + currency);
        returnedText.setText(HelperFunctions.getDecimalFormatter().format(returned) + " " + currency);
        totalText.setText(HelperFunctions.getDecimalFormatter().format(paid - returned) + " " + currency);
    }

    private void initializeTable() {
        TableColumn<CustomerTransaction, String> invoiceID = new TableColumn<>("Invoice ID");
        TableColumn<CustomerTransaction, String> amountWithCurrency = new TableColumn<>("amount");
        TableColumn<CustomerTransaction, String> isRefund = new TableColumn<>("Is a refund");
        TableColumn<CustomerTransaction, String> transactionDate = new TableColumn<>("Date/Time");

        transactionsTable.getColumns().addAll(invoiceID, amountWithCurrency, isRefund, transactionDate);

        invoiceID.setCellValueFactory(new PropertyValueFactory<>("invoiceID"));
        amountWithCurrency.setCellValueFactory(new PropertyValueFactory<>("amountWithCurrency"));
        isRefund.setCellValueFactory(new PropertyValueFactory<>("isRefund"));
        transactionDate.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

        transactionsTable.setItems(TransactionsRepository.getCustomerTransactions(selectedCustomer));

        transactionsTable.setRowFactory(tv -> {
            TableRow<CustomerTransaction> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() >= 2 && (!row.isEmpty())) {
                    CustomerTransaction transaction = row.getItem();
                    showViewSaleDialog(SalesRepository.getSale(transaction.getInvoiceID()));
                }
            });
            return row;
        });
    }

    private void showViewSaleDialog(Sale sale) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.VIEW_SALE));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            ViewSaleController controller = loader.getController();
            controller.setSale(sale);
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
