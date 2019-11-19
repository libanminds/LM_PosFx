package com.libanminds.dialogs_controllers;

import com.jfoenix.controls.JFXButton;
import com.libanminds.models.Supplier;
import com.libanminds.repositories.SupplierRepository;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private TextField comments;

    @FXML
    private TextArea address;

    @FXML
    private JFXButton cancel;

    @FXML
    private JFXButton save;

    private int supplierID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSaveButton();
        initCancelButton();
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
        } else
            supplierID = -1;
    }

    private void initSaveButton() {
        save.setOnMouseClicked((EventHandler<Event>) event -> {

            boolean successful;

            if(supplierID == -1)
                successful = SupplierRepository.addSupplier(new Supplier(
                        supplierID,
                        firstName.getText(),
                        lastName.getText(),
                        company.getText(),
                        email.getText(),
                        phone.getText(),
                        address.getText(),
                        comments.getText(),
                        0
            ));
            else
                successful = SupplierRepository.updateSupplier(new Supplier(
                        supplierID,
                        firstName.getText(),
                        lastName.getText(),
                        company.getText(),
                        email.getText(),
                        phone.getText(),
                        address.getText(),
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
