package com.libanminds.main_controllers;

import com.libanminds.dialogs_controllers.ExpenseDialogController;
import com.libanminds.dialogs_controllers.IncomeDialogController;
import com.libanminds.models.Expense;
import com.libanminds.models.Income;
import com.libanminds.repositories.ExpensesRepository;
import com.libanminds.repositories.IncomesRepository;
import com.libanminds.utils.Views;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class IncomeController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private Button deleteIncome;

    @FXML
    private Button editIncome;

    @FXML
    private Button addIncomeButton;

    @FXML
    private TableView<Income> incomesTable;

    Income selectedIncome;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initSearch();
        initNewIncome();
        setTableListener();
    }

    private void initNewIncome() {
        addIncomeButton.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                showIncomeDialog(null);
            }
        });

        editIncome.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                showIncomeDialog(selectedIncome);
            }
        });

        deleteIncome.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                showDeleteConfirmationDialog();
            }
        });

        deleteIncome.setDisable(selectedIncome == null);
        editIncome.setDisable(selectedIncome == null);
    }

    private void showIncomeDialog(Income income) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.ADD_INCOME));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            IncomeDialogController controller = loader.getController();
            controller.initData(income);
            stage.show();
            stage.setOnHidden(e -> {
                incomesTable.setItems(IncomesRepository.getIncomes());
            });
        }catch (Exception e){}
    }

    private void showDeleteConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete this income row?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == yesButton) {
            boolean successful = IncomesRepository.deleteIncome(selectedIncome);
            if(successful)
                incomesTable.getItems().remove(selectedIncome);
        } else {
            alert.close();
        }
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

    private void setTableListener() {
        incomesTable.selectionModelProperty().get().selectedItemProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
            selectedIncome = (Income)newValue;
            deleteIncome.setDisable(selectedIncome == null);
            editIncome.setDisable(selectedIncome == null);
        });
    }
}
