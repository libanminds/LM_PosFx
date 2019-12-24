package com.libanminds.dialogs_controllers;

import com.libanminds.models.Supplier;
import com.libanminds.repositories.SupplierRepository;
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

public class SupplierDialogController implements Initializable {

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


    private int supplierID = -1;

    private SelectSupplierDialogController hostController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSaveButton();
        initCancelButton();
        initErrorMessages();
    }

    public void setHostController(SelectSupplierDialogController controller) {
        hostController = controller;
    }

    public void initData(Supplier supplier) {
        if (supplier != null) {
            supplierID = supplier.getID() ;
            firstName.setText(supplier.getFirstName());
            lastName.setText(supplier.getLastName());
            company.setText(supplier.getCompany());
            email.setText(supplier.getEmail());
            phone.setText(supplier.getPhone());
            address.setText(supplier.getAddress());
            comments.setText(supplier.getNotes());
        }
    }

    private void initErrorMessages() {
        errorMessagesLabel.setText("");
    }

    private boolean validateInput() {
        if(firstName.getText().isEmpty()) {
            errorMessagesLabel.setText("Please fill in all the required fields");
            HelperFunctions.highlightTextfieldError(firstName);
            return false;
        }

        return true;
    }

    private void initSaveButton() {

        save.setOnMouseClicked((EventHandler<Event>) event -> {
            if(!validateInput()) return;

            boolean successful;

            Supplier supplier = new Supplier(
                    supplierID,
                    firstName.getText(),
                    lastName.getText(),
                    company.getText(),
                    email.getText(),
                    phone.getText(),
                    address.getText(),
                    comments.getText(),
                    0
            );

            if(supplierID == -1)
                successful = SupplierRepository.addSupplier(supplier);
            else
                successful = SupplierRepository.updateSupplier(supplier);

            if(successful) {
                if(hostController != null) {
                    hostController.setSelectedSupplier(supplier);
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
