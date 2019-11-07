package com.libanminds.main_controllers;

import com.libanminds.models.Expense;
import com.libanminds.models.User;
import com.libanminds.repositories.ExpensesRepository;
import com.libanminds.repositories.UsersRepository;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ExpensesController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private Button addExpenseButton;

    @FXML
    private TableView<Expense> expensesTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initSearch();
    }

    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            expensesTable.setItems(ExpensesRepository.getExpensesLike(newValue));
        });
    }

    private void initializeTable() {
        TableColumn<Expense,String> typeCol = new TableColumn<>("Type");
        TableColumn<Expense,String> descriptionCol = new TableColumn<>("Description");
        TableColumn<Expense,String> amountCol = new TableColumn<>("Amount");
        TableColumn<Expense,String> paymentTypeCol = new TableColumn<>("Payment Type");
        TableColumn<Expense,String> recipientCol = new TableColumn<>("Recipient");
        TableColumn<Expense,String> notesCol = new TableColumn<>("Notes");

        expensesTable.getColumns().addAll(typeCol,descriptionCol,amountCol,paymentTypeCol,recipientCol,notesCol);

        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amountWithCurrency"));
        paymentTypeCol.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        recipientCol.setCellValueFactory(new PropertyValueFactory<>("recipient"));
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));

        expensesTable.setItems(ExpensesRepository.getExpenses());
    }
}
