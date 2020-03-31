package com.libanminds.controllers.main;

import com.libanminds.constants.AuthorizationKeys;
import com.libanminds.constants.Views;
import com.libanminds.controllers.dialogs.CustomerDialogController;
import com.libanminds.controllers.transaction_history.CustomerStatementOfAccountController;
import com.libanminds.models.Customer;
import com.libanminds.repositories.CustomersRepository;
import com.libanminds.utils.Authorization;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomersController implements Initializable {
    @FXML private HBox buttonsHolder;
    @FXML private TextField searchField;
    @FXML private Button deleteCustomer;
    @FXML private Button editCustomer;
    @FXML private Button newCustomerBtn;
    @FXML private Button accountStatementBtn;
    @FXML private TableView<Customer> customersTable;

    private Customer selectedCustomer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtons();
        initSearch();
        initializeTable();
        setTableListener();
        handleAuthorization();
    }

    private void handleAuthorization() {
        if (Authorization.cannot(AuthorizationKeys.VIEW_CUSTOMER_STATEMENT_OF_ACCOUNT))
            buttonsHolder.getChildren().remove(accountStatementBtn);
        if (Authorization.cannot(AuthorizationKeys.ADD_CUSTOMER))
            buttonsHolder.getChildren().remove(newCustomerBtn);
        if (Authorization.cannot(AuthorizationKeys.EDIT_CUSTOMER))
            buttonsHolder.getChildren().remove(editCustomer);
        if (Authorization.cannot(AuthorizationKeys.DELETE_CUSTOMER))
            buttonsHolder.getChildren().remove(deleteCustomer);
    }

    private void initializeVariables() {
        selectedCustomer = null;
    }

    private void initButtons() {
        newCustomerBtn.setOnMouseClicked((EventHandler<Event>) event -> showCustomerDialog(null));
        editCustomer.setOnMouseClicked((EventHandler<Event>) event -> showCustomerDialog(selectedCustomer));
        deleteCustomer.setOnMouseClicked((EventHandler<Event>) event -> showDeleteConfirmationDialog());
        accountStatementBtn.setOnMouseClicked((EventHandler<Event>) event -> showStatementOfAccountDialog(selectedCustomer));

        deleteCustomer.setDisable(selectedCustomer == null);
        editCustomer.setDisable(selectedCustomer == null);
        accountStatementBtn.setDisable(selectedCustomer == null);
    }

    private void showCustomerDialog(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.ADD_CUSTOMER));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            CustomerDialogController controller = loader.getController();
            controller.initData(customer);
            stage.show();
            stage.setOnHidden(e -> customersTable.setItems(CustomersRepository.getCustomers()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showStatementOfAccountDialog(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.CUSTOMER_STATEMENT_OF_ACCOUNT));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            CustomerStatementOfAccountController controller = loader.getController();
            controller.setSelectedCustomer(customer);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDeleteConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delele " + selectedCustomer.getName() + "?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == yesButton) {
            boolean successful = CustomersRepository.deleteCustomer(selectedCustomer);
            if (successful)
                customersTable.getItems().remove(selectedCustomer);
        } else {
            alert.close();
        }
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

        customersTable.setItems(CustomersRepository.getCustomers());
    }

    private void setTableListener() {
        customersTable.selectionModelProperty().get().selectedItemProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
            selectedCustomer = (Customer) newValue;
            deleteCustomer.setDisable(selectedCustomer == null);
            editCustomer.setDisable(selectedCustomer == null);
            accountStatementBtn.setDisable(selectedCustomer == null);
        });
    }
}
