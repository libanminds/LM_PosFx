package com.libanminds.controllers.main;

import com.libanminds.constants.AuthorizationKeys;
import com.libanminds.constants.Views;
import com.libanminds.controllers.dialogs.ExpenseDialogController;
import com.libanminds.models.Expense;
import com.libanminds.repositories.ExpensesRepository;
import com.libanminds.utils.Authorization;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ExpensesController implements Initializable {
    @FXML private HBox buttonsHolder;
    @FXML private TextField searchField;
    @FXML private Button deleteExpenses;
    @FXML private Button editExpenses;
    @FXML private Button addExpenseButton;
    @FXML private MenuItem manageTypes;
    @FXML private MenuButton moreOptions;
    @FXML private TableView<Expense> expensesTable;

    private Expense selectedExpense;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initSearch();
        initButtons();
        setTableListener();
        handleAuthorization();
    }

    private void handleAuthorization() {
        if (Authorization.cannot(AuthorizationKeys.ADD_EXPENSES)) buttonsHolder.getChildren().remove(addExpenseButton);
        if (Authorization.cannot(AuthorizationKeys.EDIT_EXPENSES)) buttonsHolder.getChildren().remove(editExpenses);
        if (Authorization.cannot(AuthorizationKeys.DELETE_EXPENSES)) buttonsHolder.getChildren().remove(deleteExpenses);
        moreOptions.setVisible(Authorization.can(AuthorizationKeys.VIEW_EXPENSES_CATEGORIES));
    }

    private void initButtons() {
        addExpenseButton.setOnMouseClicked((EventHandler<Event>) event -> showCustomerDialog(null));
        editExpenses.setOnMouseClicked((EventHandler<Event>) event -> showCustomerDialog(selectedExpense));
        deleteExpenses.setOnMouseClicked((EventHandler<Event>) event -> showDeleteConfirmationDialog());
        manageTypes.setOnAction(t -> showExpenseTypesDialog());
        deleteExpenses.setDisable(selectedExpense == null);
        editExpenses.setDisable(selectedExpense == null);
    }

    private void showExpenseTypesDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.EXPENSE_TYPES));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    private void showCustomerDialog(Expense expense) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.ADD_EXPENSE));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            ExpenseDialogController controller = loader.getController();
            controller.initData(expense);
            stage.show();
            stage.setOnHidden(e -> expensesTable.setItems(ExpensesRepository.getExpenses()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDeleteConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delele this expense row?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == yesButton) {
            boolean successful = ExpensesRepository.deleteExpense(selectedExpense);
            if (successful)
                expensesTable.getItems().remove(selectedExpense);
        } else {
            alert.close();
        }
    }

    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            expensesTable.setItems(ExpensesRepository.getExpensesLike(newValue));
        });
    }

    private void initializeTable() {
        TableColumn<Expense, String> typeCol = new TableColumn<>("Type");
        TableColumn<Expense, String> descriptionCol = new TableColumn<>("Description");
        TableColumn<Expense, String> amountCol = new TableColumn<>("Amount");
        TableColumn<Expense, String> paymentTypeCol = new TableColumn<>("Payment Type");
        TableColumn<Expense, String> recipientCol = new TableColumn<>("Recipient");
        TableColumn<Expense, String> notesCol = new TableColumn<>("Notes");

        expensesTable.getColumns().addAll(typeCol, descriptionCol, amountCol, paymentTypeCol, recipientCol, notesCol);

        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amountWithCurrency"));
        paymentTypeCol.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        recipientCol.setCellValueFactory(new PropertyValueFactory<>("recipient"));
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));

        expensesTable.setItems(ExpensesRepository.getExpenses());
    }

    private void setTableListener() {
        expensesTable.selectionModelProperty().get().selectedItemProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
            selectedExpense = (Expense) newValue;
            deleteExpenses.setDisable(selectedExpense == null);
            editExpenses.setDisable(selectedExpense == null);
        });
    }
}
