package com.libanminds.dialogs_controllers;

import com.libanminds.models.Expense;
import com.libanminds.models.Type;
import com.libanminds.repositories.ExpenseTypesRepository;
import com.libanminds.repositories.ExpensesRepository;
import com.libanminds.utils.Constants;
import com.libanminds.utils.GlobalSettings;
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

public class ExpenseDialogController implements Initializable {

    @FXML
    private TextField description;

    @FXML
    private TextField amount;

    @FXML
    private TextField recipient;

    @FXML
    private TextArea notes;

    @FXML
    private Button cancel;

    @FXML
    private Button save;

    @FXML
    private ChoiceBox<Type> typeChoiceBox;

    @FXML
    private ChoiceBox<String> currency;

    @FXML
    private ChoiceBox<String> paymentType;

    @FXML
    private Label errorMessagesLabel;

    private int expenseID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initChoiceBoxes();
        initSaveButton();
        initCancelButton();
        initFilters();
        initErrorMessages();
    }

    public void initData(Expense expense) {
        if (expense != null) {
            expenseID = expense.getID();

            typeChoiceBox.setValue(expense.getTypeObject());
            description.setText(expense.getDescription());
            amount.setText(expense.getAmount() + "");
            currency.setValue(expense.getCurrency());
            paymentType.setValue(expense.getPaymentType());
            recipient.setText(expense.getRecipient());
            notes.setText(expense.getNotes());
        } else
            expenseID = -1;
    }

    private void initErrorMessages() {
        errorMessagesLabel.setText("");
    }

    private void initFilters() {
        amount.setTextFormatter(HelperFunctions.getUnsignedNumberFilter());
    }

    private void initChoiceBoxes() {
        ObservableList<Type> types = ExpenseTypesRepository.getExpenseTypes();
        String[] currencies = {Constants.DOLLAR_CURRENCY, Constants.LIRA_CURRENCY};
        String[] payments = Constants.paymentTypes;

        typeChoiceBox.setItems(FXCollections.observableList(types));
        typeChoiceBox.setValue(types.get(0));

        currency.setItems(FXCollections.observableArrayList(currencies));
        currency.setValue(GlobalSettings.DEFAULT_CURRENCY);

        paymentType.setItems(FXCollections.observableArrayList(payments));
        paymentType.setValue(GlobalSettings.DEFAULT_PAYMENT_TYPE);
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

            if (expenseID == -1)
                successful = ExpensesRepository.addExpense(new Expense(
                        expenseID,
                        typeChoiceBox.getValue(),
                        description.getText(),
                        Double.parseDouble(amount.getText()),
                        currency.getValue(),
                        paymentType.getValue(),
                        recipient.getText(),
                        notes.getText()
                ));
            else
                successful = ExpensesRepository.updateExpense(new Expense(
                        expenseID,
                        typeChoiceBox.getValue(),
                        description.getText(),
                        Double.parseDouble(amount.getText()),
                        currency.getValue(),
                        paymentType.getValue(),
                        recipient.getText(),
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
