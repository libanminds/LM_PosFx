package com.libanminds.dialogs_controllers;

import com.libanminds.models.Customer;
import com.libanminds.repositories.CustomersRepository;
import com.libanminds.utils.HelperFunctions;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerDialogController implements Initializable {

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField email;

    @FXML
    private TextField phone;

    @FXML
    private TextField company;

    @FXML
    private TextArea comments;

    @FXML
    private TextField address;

    @FXML
    private Button cancel;

    @FXML
    private Button save;

    @FXML
    private Label errorMessagesLabel;


    private int customerID = -1;

    private SelectCustomerDialogController hostController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSaveButton();
        initCancelButton();
        initErrorMessages();
    }

    public void setHostController(SelectCustomerDialogController controller) {
        hostController = controller;
    }

    public void initData(Customer customer) {

        if (customer != null) {
            customerID = customer.getID();
            firstName.setText(customer.getFirstName());
            lastName.setText(customer.getLastName());
            email.setText(customer.getEmail());
            phone.setText(customer.getPhone());
            address.setText(customer.getAddress());
            company.setText(customer.getCompany());
            comments.setText(customer.getNotes());
        }
    }

    private void initErrorMessages() {
        errorMessagesLabel.setText("");
    }

    private void initSaveButton() {
        save.setOnMouseClicked((EventHandler<Event>) event -> {

            if (!validateInput())
                return;

            boolean successful;
            Customer newCustomer = new Customer(
                    customerID,
                    firstName.getText(),
                    lastName.getText(),
                    email.getText(),
                    phone.getText(),
                    address.getText(),
                    company.getText(),
                    comments.getText(),
                    0
            );

            if (customerID == -1)
                successful = CustomersRepository.addCustomer(newCustomer);
            else
                successful = CustomersRepository.updateCustomer(newCustomer);

            if (successful) {
                if (hostController != null) {
                    hostController.setSelectedCustomer(newCustomer);
                }
                Stage currentStage = (Stage) save.getScene().getWindow();
                currentStage.close();
            } else {
                //TODO : DO SOMETHING
            }
        });
    }

    private boolean validateInput() {
        if (firstName.getText().isEmpty()) {
            errorMessagesLabel.setText("Please fill in all the required fields");
            HelperFunctions.highlightTextfieldError(firstName);
            return false;
        }

        return true;
    }

    private void initCancelButton() {
        cancel.setOnMouseClicked((EventHandler<Event>) event -> {
            Stage currentStage = (Stage) save.getScene().getWindow();
            currentStage.close();
        });
    }
}
