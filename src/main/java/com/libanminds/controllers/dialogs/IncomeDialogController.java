package com.libanminds.controllers.dialogs;

import com.libanminds.constants.Constants;
import com.libanminds.models.Income;
import com.libanminds.models.Type;
import com.libanminds.repositories.IncomeTypesRepository;
import com.libanminds.repositories.IncomesRepository;
import com.libanminds.singletons.GlobalSettings;
import com.libanminds.utils.HelperFunctions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class IncomeDialogController implements Initializable {
    @FXML private TextField description;
    @FXML private TextField amount;
    @FXML private TextField from;
    @FXML private TextArea notes;
    @FXML private Button cancel;
    @FXML private Button save;
    @FXML private ChoiceBox<Type> typeChoiceBox;
    @FXML private ChoiceBox<String> currency;
    @FXML private ChoiceBox<String> paymentType;
    @FXML private Label errorMessagesLabel;

    private int incomeID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initChoiceBoxes();
        initSaveButton();
        initCancelButton();
        initFilters();
        initErrorMessages();
    }

    public void initData(Income income) {
        if (income != null) {
            incomeID = income.getID();

            typeChoiceBox.setValue(income.getTypeObject());
            description.setText(income.getDescription());
            amount.setText(income.getAmount() + "");
            currency.setValue(income.getCurrency());
            paymentType.setValue(income.getPaymentType());
            from.setText(income.getFrom());
            notes.setText(income.getNotes());
        } else
            incomeID = -1;
    }

    private void initErrorMessages() {
        errorMessagesLabel.setText("");
    }

    private void initFilters() {
        amount.setTextFormatter(HelperFunctions.getUnsignedNumberFilter());
    }

    private void initChoiceBoxes() {
        ObservableList<Type> types = IncomeTypesRepository.getIncomeTypes();
        String[] currencies = {Constants.USD_CURRENCY, Constants.LBP_CURRENCY};
        String[] payments = Constants.paymentTypes;

        typeChoiceBox.setItems(FXCollections.observableList(types));
        typeChoiceBox.setValue(types.get(0));

        currency.setItems(FXCollections.observableArrayList(currencies));
        currency.setValue(GlobalSettings.fetch().defaultCurrency);

        paymentType.setItems(FXCollections.observableArrayList(payments));
        paymentType.setValue(GlobalSettings.fetch().defaultPaymentType);
    }

    private boolean validateInput() {
        boolean isValid = true;
        if (description.getText().isEmpty()) {
            HelperFunctions.highlightTextfieldError(description);
            isValid = false;
        }

        if (amount.getText().isEmpty()) {
            HelperFunctions.highlightTextfieldError(amount);
            isValid = false;
        }

        if (!isValid) errorMessagesLabel.setText("Please fill in all the required fields");
        return isValid;
    }

    private void initSaveButton() {
        save.setOnMouseClicked((EventHandler<Event>) event -> {
            if (!validateInput()) return;
            boolean successful;

            if (incomeID == -1)
                successful = IncomesRepository.addIncome(new Income(
                        incomeID,
                        typeChoiceBox.getValue(),
                        description.getText(),
                        Double.parseDouble(amount.getText()),
                        currency.getValue(),
                        paymentType.getValue(),
                        from.getText(),
                        notes.getText()
                ));
            else
                successful = IncomesRepository.updateIncome(new Income(
                        incomeID,
                        typeChoiceBox.getValue(),
                        description.getText(),
                        Double.parseDouble(amount.getText()),
                        currency.getValue(),
                        paymentType.getValue(),
                        from.getText(),
                        notes.getText()
                ));

            if (successful) {
                Stage currentStage = (Stage) save.getScene().getWindow();
                currentStage.close();
            } else {
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
