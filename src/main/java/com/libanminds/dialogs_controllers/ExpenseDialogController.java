package com.libanminds.dialogs_controllers;

import com.jfoenix.controls.JFXButton;
import com.libanminds.models.Customer;
import com.libanminds.models.Expense;
import com.libanminds.models.Type;
import com.libanminds.repositories.CustomersRepository;
import com.libanminds.repositories.ExpensesRepository;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

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
    private JFXButton cancel;

    @FXML
    private JFXButton save;

    @FXML
    private ChoiceBox<Type> typeChoiceBox;

    @FXML
    private ChoiceBox<String> currency;

    @FXML
    private ChoiceBox<String> paymentType;

    private int expenseID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initChoiceBoxes();
        initSaveButton();
        initCancelButton();
    }

    public void initData(Expense expense) {
        if (expense != null) {
            expenseID = expense.getID();

            //typeChoiceBox.setValue();
            description.setText(expense.getDescription());
            amount.setText(expense.getAmount() + "");
            currency.setValue(expense.getCurrency());
            paymentType.setValue(expense.getPaymentType());
            recipient.setText(expense.getRecipient());
            notes.setText(expense.getNotes());
        } else
            expenseID = -1;
    }

    private void initChoiceBoxes() {
        Type[] types = {
                new Type(1,"Type1")
        };
        String[] currencies = { "$", "LL" };
        String[] payments = { "Cash", "Cheque", "Credit Card" };

        typeChoiceBox.setItems(FXCollections.observableArrayList(types));
        currency.setItems(FXCollections.observableArrayList(currencies));
        paymentType.setItems(FXCollections.observableArrayList(payments));

        //This statements makes sure that the string 'name' is displayed in the UI.
        typeChoiceBox.setConverter(new StringConverter<>() {

            @Override
            public String toString(Type object) {
                return object.getName();
            }

            @Override
            public Type fromString(String val) {
                return typeChoiceBox.getItems().stream().filter(ap ->
                        ap.getName().equals(val)).findFirst().orElse(null);
            }
        });
    }

    private void initSaveButton() {
        save.setOnMouseClicked((EventHandler<Event>) event -> {
            boolean successful;

            if(expenseID == -1)
                successful = ExpensesRepository.addExpense(new Expense(
                        expenseID,
                        typeChoiceBox.getValue().getName(),
                        description.getText(),
                        Double.parseDouble(amount.getText()),
                        currency.getValue(),
                        paymentType.getValue(),
                        recipient.getText(),
                        notes.getText()
                ),typeChoiceBox.getValue().getID());
            else
                successful = ExpensesRepository.updateExpense(new Expense(
                        expenseID,
                        typeChoiceBox.getValue().getName(),
                        description.getText(),
                        Double.parseDouble(amount.getText()),
                        currency.getValue(),
                        paymentType.getValue(),
                        recipient.getText(),
                        notes.getText()
                ),typeChoiceBox.getValue().getID());

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
