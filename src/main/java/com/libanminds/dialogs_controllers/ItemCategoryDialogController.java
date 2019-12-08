package com.libanminds.dialogs_controllers;

import com.jfoenix.controls.JFXButton;
import com.libanminds.models.Expense;
import com.libanminds.models.Income;
import com.libanminds.models.ItemCategory;
import com.libanminds.models.Type;
import com.libanminds.repositories.ExpensesRepository;
import com.libanminds.repositories.IncomesRepository;
import com.libanminds.repositories.ItemsCategoriesRepository;
import com.libanminds.utils.HelperFunctions;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class ItemCategoryDialogController implements Initializable {

    @FXML
    private TextField categoryField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label errorMessagesLabel;

    private int categoryID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSaveButton();
        initCancelButton();
        initErrorMessages();
    }

    public void initData(ItemCategory category) {
        if (category != null) {
            categoryID = category.getID();
            categoryField.setText(category.getName());
        } else
            categoryID = -1;
    }

    private void initErrorMessages() {
        errorMessagesLabel.setText("");
    }

    private boolean validateInput() {
        boolean isValid = true;
        if(categoryField.getText().isEmpty()) {
            HelperFunctions.highlightTextfieldError(categoryField);
            isValid = false;
        }

        if(!isValid) errorMessagesLabel.setText("Please fill in the required field");
        return isValid;
    }

    private void initSaveButton() {
        saveButton.setOnMouseClicked((EventHandler<Event>) event -> {
            if(!validateInput()) return;
            boolean successful;

            if(categoryID == -1)
                successful = ItemsCategoriesRepository.addItemCategory(new ItemCategory(
                        categoryID,
                        categoryField.getText()
                ));
            else
                successful = ItemsCategoriesRepository.updateItemCategory(new ItemCategory(
                        categoryID,
                        categoryField.getText()
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
