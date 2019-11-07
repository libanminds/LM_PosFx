package com.libanminds.main_controllers;

import com.libanminds.models.Expense;
import com.libanminds.models.Income;
import com.libanminds.repositories.ExpensesRepository;
import com.libanminds.repositories.IncomesRepository;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class IncomeController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private Button addIncomeButton;

    @FXML
    private TableView<Income> incomesTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initSearch();
    }

    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            incomesTable.setItems(IncomesRepository.getIncomesLike(newValue));
        });
    }

    private void initializeTable() {
        TableColumn<Income,String> typeCol = new TableColumn<>("Type");
        TableColumn<Income,String> descriptionCol = new TableColumn<>("Description");
        TableColumn<Income,String> amountCol = new TableColumn<>("Amount");
        TableColumn<Income,String> paymentTypeCol = new TableColumn<>("Payment Type");
        TableColumn<Income,String> fromCol = new TableColumn<>("From");
        TableColumn<Income,String> notesCol = new TableColumn<>("Notes");

        incomesTable.getColumns().addAll(typeCol,descriptionCol,amountCol,paymentTypeCol,fromCol,notesCol);

        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amountWithCurrency"));
        paymentTypeCol.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        fromCol.setCellValueFactory(new PropertyValueFactory<>("from"));
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));

        incomesTable.setItems(IncomesRepository.getIncomes());
    }
}
