package com.libanminds.controllers.main;

import com.libanminds.constants.AuthorizationKeys;
import com.libanminds.constants.Views;
import com.libanminds.controllers.dialogs.IncomeDialogController;
import com.libanminds.models.Income;
import com.libanminds.repositories.IncomesRepository;
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

public class IncomeController implements Initializable {
    Income selectedIncome;
    @FXML private HBox buttonsHolder;
    @FXML private TextField searchField;
    @FXML private Button deleteIncome;
    @FXML private Button editIncome;
    @FXML private Button addIncomeButton;
    @FXML private MenuItem manageTypes;
    @FXML private MenuButton moreOptions;
    @FXML private TableView<Income> incomesTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initSearch();
        initNewIncome();
        setTableListener();
        handleAuthorization();
    }

    private void handleAuthorization() {
        if (Authorization.cannot(AuthorizationKeys.ADD_INCOME)) buttonsHolder.getChildren().remove(addIncomeButton);
        if (Authorization.cannot(AuthorizationKeys.EDIT_INCOME)) buttonsHolder.getChildren().remove(editIncome);
        if (Authorization.cannot(AuthorizationKeys.DELETE_INCOME)) buttonsHolder.getChildren().remove(deleteIncome);
        moreOptions.setVisible(Authorization.can(AuthorizationKeys.VIEW_INCOMES_CATEGORIES));
    }

    private void initNewIncome() {
        addIncomeButton.setOnMouseClicked((EventHandler<Event>) event -> showIncomeDialog(null));
        editIncome.setOnMouseClicked((EventHandler<Event>) event -> showIncomeDialog(selectedIncome));
        deleteIncome.setOnMouseClicked((EventHandler<Event>) event -> showDeleteConfirmationDialog());
        manageTypes.setOnAction(t -> showIncomeTypesDialog());

        deleteIncome.setDisable(selectedIncome == null);
        editIncome.setDisable(selectedIncome == null);
    }

    private void showIncomeTypesDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.INCOME_TYPES));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (Exception e) {
           e.printStackTrace();
        }
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
            stage.setOnHidden(e -> incomesTable.setItems(IncomesRepository.getIncomes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            if (successful)
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
        TableColumn<Income, String> typeCol = new TableColumn<>("Type");
        TableColumn<Income, String> descriptionCol = new TableColumn<>("Description");
        TableColumn<Income, String> amountCol = new TableColumn<>("Amount");
        TableColumn<Income, String> paymentTypeCol = new TableColumn<>("Payment Type");
        TableColumn<Income, String> fromCol = new TableColumn<>("From");
        TableColumn<Income, String> notesCol = new TableColumn<>("Notes");

        incomesTable.getColumns().addAll(typeCol, descriptionCol, amountCol, paymentTypeCol, fromCol, notesCol);

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
            selectedIncome = (Income) newValue;
            deleteIncome.setDisable(selectedIncome == null);
            editIncome.setDisable(selectedIncome == null);
        });
    }
}
