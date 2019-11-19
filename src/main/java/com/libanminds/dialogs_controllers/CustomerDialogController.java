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

    private int customerID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSaveButton();
        initCancelButton();
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
        } else
            customerID = -1;
    }


    private void initSaveButton() {
        save.setOnMouseClicked((EventHandler<Event>) event -> {

            boolean successful;

            if(customerID == -1)
            successful = CustomersRepository.addCustomer(new Customer(
                    customerID,
                    firstName.getText(),
                    lastName.getText(),
                    email.getText(),
                    phone.getText(),
                    address.getText(),
                    company.getText(),
                    comments.getText(),
                    0
            ));
            else
                successful = CustomersRepository.updateCustomer(new Customer(
                        customerID,
                        firstName.getText(),
                        lastName.getText(),
                        email.getText(),
                        phone.getText(),
                        address.getText(),
                        company.getText(),
                        comments.getText(),
                        0
                ));

            if(successful) {
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
