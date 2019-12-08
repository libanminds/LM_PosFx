package com.libanminds.dialogs_controllers;

import com.libanminds.models.Type;
import com.libanminds.repositories.ExpenseTypesRepository;
import com.libanminds.repositories.IncomeTypesRepository;
import com.libanminds.utils.HelperFunctions;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ExpenseTypesDialogController implements Initializable {

    @FXML
    private TextField typeField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label errorMessagesLabel;

    private int typeID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSaveButton();
        initCancelButton();
        initErrorMessages();
    }

    public void initData(Type type) {
        if (type != null) {
            typeID = type.getID();
            typeField.setText(type.getName());
        } else
            typeID = -1;
    }

    private void initErrorMessages() {
        errorMessagesLabel.setText("");
    }

    private boolean validateInput() {
        boolean isValid = true;
        if(typeField.getText().isEmpty()) {
            HelperFunctions.highlightTextfieldError(typeField);
            isValid = false;
        }

        if(!isValid) errorMessagesLabel.setText("Please fill in the required field");
        return isValid;
    }

    private void initSaveButton() {
        saveButton.setOnMouseClicked((EventHandler<Event>) event -> {
            if(!validateInput()) return;
            boolean successful;

            if(typeID == -1)
                successful = ExpenseTypesRepository.addExpenseType(new Type(
                        typeID,
                        typeField.getText()
                ));
            else
                successful = ExpenseTypesRepository.updateExpenseType(new Type(
                        typeID,
                        typeField.getText()
                ));

            if(successful) {
                Stage currentStage = (Stage) saveButton.getScene().getWindow();
                currentStage.close();
            }else {
                //TODO : DO SOMETHING
            }

        });
    }

    private void initCancelButton() {
        cancelButton.setOnMouseClicked((EventHandler<Event>) event -> {
            Stage currentStage = (Stage) cancelButton.getScene().getWindow();
            currentStage.close();
        });
    }
}
