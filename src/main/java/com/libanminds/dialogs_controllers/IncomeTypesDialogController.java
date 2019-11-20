package com.libanminds.dialogs_controllers;

import com.libanminds.models.ItemCategory;
import com.libanminds.models.Type;
import com.libanminds.repositories.IncomeTypesRepository;
import com.libanminds.repositories.ItemsCategoriesRepository;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class IncomeTypesDialogController implements Initializable {

    @FXML
    private TextField typeField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private int typeID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSaveButton();
        initCancelButton();
    }

    public void initData(Type type) {
        if (type != null) {
            typeID = type.getID();
            typeField.setText(type.getName());
        } else
            typeID = -1;
    }

    private void initSaveButton() {
        saveButton.setOnMouseClicked((EventHandler<Event>) event -> {
            boolean successful;

            if(typeID == -1)
                successful = IncomeTypesRepository.addIncomeType(new Type(
                        typeID,
                        typeField.getText()
                ));
            else
                successful = IncomeTypesRepository.updateIncomeType(new Type(
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
