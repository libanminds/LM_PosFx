package com.libanminds.dialogs_controllers;

import com.jfoenix.controls.JFXButton;
import com.libanminds.models.Customer;
import com.libanminds.repositories.CustomersRepository;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private TextField comments;

    @FXML
    private TextArea address;

    @FXML
    private JFXButton cancel;

    @FXML
    private JFXButton save;

    private int customerID = -1;

    private SelectCustomerDialogController hostController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSaveButton();
        initCancelButton();
    }

    public void setHostController(SelectCustomerDialogController controller) {
        hostController = controller;
    }

    public void initData(Customer customer) {
        if (customer != null) {
            customerID = customer.getID() ;
            firstName.setText(customer.getFirstName());
            lastName.setText(customer.getLastName());
            email.setText(customer.getEmail());
            phone.setText(customer.getPhone());
            address.setText(customer.getAddress());
            company.setText(customer.getCompany());
            comments.setText(customer.getNotes());
        }
    }

    private void initSaveButton() {
        save.setOnMouseClicked((EventHandler<Event>) event -> {

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

            if(customerID == -1)
            successful = CustomersRepository.addCustomer(newCustomer);
            else
                successful = CustomersRepository.updateCustomer(newCustomer);

            if(successful) {
                if(hostController != null) {
                    hostController.setSelectedCustomer(newCustomer);
                }
                Stage currentStage = (Stage) save.getScene().getWindow();
                currentStage.close();
            }else {
                //TODO : DO SOMETHING
            }
        });
    }

    private void initCancelButton() {
        cancel.setOnMouseClicked((EventHandler<Event>) event -> {
            Stage currentStage = (Stage) save.getScene().getWindow();
            currentStage.close();
        });
    }
}
