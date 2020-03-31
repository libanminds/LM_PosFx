package com.libanminds.controllers.dialogs;

import com.libanminds.constants.AuthorizationKeys;
import com.libanminds.constants.Views;
import com.libanminds.controllers.main.NewSaleController;
import com.libanminds.controllers.reports.CustomersReportController;
import com.libanminds.models.Customer;
import com.libanminds.repositories.CustomersRepository;
import com.libanminds.utils.Authorization;
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

public class SelectCustomerDialogController implements Initializable {
    @FXML private TextField searchField;
    @FXML private Button newCustomerBtn;
    @FXML private TableView<Customer> customersTable;

    private Customer selectedCustomer;
    private NewSaleController saleController;
    private CustomersReportController reportsController;
    private boolean fromReports;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtons();
        initSearch();
        initializeTable();
        handleAuthorization();
        fromReports = false;
    }

    public void setSaleController(NewSaleController controller) {
        saleController = controller;
    }

    private void handleAuthorization() {
        newCustomerBtn.setVisible(Authorization.can(AuthorizationKeys.ADD_CUSTOMER));
    }

    private void initButtons() {
        newCustomerBtn.setOnMouseClicked((EventHandler<Event>) event -> showNewCustomerDialog());
    }

    private void showNewCustomerDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.ADD_CUSTOMER));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            CustomerDialogController controller = loader.getController();
            controller.setHostController(this);
            stage.show();
            stage.setOnHidden(e -> {
                if (selectedCustomer != null) {
                    sendDataBackToHost();
                    Stage currentStage = (Stage) newCustomerBtn.getScene().getWindow();
                    currentStage.close();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSelectedCustomer(Customer customer) {
        selectedCustomer = customer;
    }

    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            customersTable.setItems(CustomersRepository.getCustomersLike(newValue));
        });
    }

    private void initializeTable() {
        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        TableColumn<Customer, String> emailCol = new TableColumn<>("Email");
        TableColumn<Customer, String> phoneCol = new TableColumn<>("Phone");
        TableColumn<Customer, String> addressCol = new TableColumn<>("Address");
        TableColumn<Customer, Double> balanceCol = new TableColumn<>("Balance");

        customersTable.getColumns().addAll(nameCol, emailCol, phoneCol, addressCol, balanceCol);

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));

        customersTable.setRowFactory(tv -> {
            TableRow<Customer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() >= 2 && (!row.isEmpty())) {
                    Customer customer = row.getItem();
                    setSelectedCustomer(customer);
                    sendDataBackToHost();
                    Stage currentStage = (Stage) searchField.getScene().getWindow();
                    currentStage.close();
                }
            });
            return row;
        });

        customersTable.setItems(CustomersRepository.getCustomers());
    }

    private void sendDataBackToHost() {
        if (fromReports) {
            reportsController.setSelectedCustomer(selectedCustomer);
        } else {
            saleController.setSelectedCustomer(selectedCustomer);
        }
    }

    public void setFromReports(boolean fromReports) {
        this.fromReports = fromReports;
        newCustomerBtn.setVisible(!fromReports);
    }

    public void setReportsController(CustomersReportController reportsController) {
        this.reportsController = reportsController;
    }
}
