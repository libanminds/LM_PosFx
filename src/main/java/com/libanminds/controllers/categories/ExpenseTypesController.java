package com.libanminds.controllers.categories;

import com.libanminds.constants.AuthorizationKeys;
import com.libanminds.constants.Views;
import com.libanminds.controllers.dialogs.ExpenseTypesDialogController;
import com.libanminds.models.Type;
import com.libanminds.repositories.ExpenseTypesRepository;
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

public class ExpenseTypesController implements Initializable {

    @FXML private HBox buttonsHolder;
    @FXML private TextField searchField;
    @FXML private Button deleteType;
    @FXML private Button editType;
    @FXML private Button addType;
    @FXML private TableView<Type> typesTable;

    private Type selectedType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtons();
        initSearch();
        initializeTable();
        setTableListener();
        handleAuthorization();
    }

    private void handleAuthorization() {
        if (Authorization.cannot(AuthorizationKeys.ADD_EXPENSES_CATEGORIES))
            buttonsHolder.getChildren().remove(addType);
        if (Authorization.cannot(AuthorizationKeys.EDIT_EXPENSES_CATEGORIES))
            buttonsHolder.getChildren().remove(editType);
        if (Authorization.cannot(AuthorizationKeys.DELETE_EXPENSES_CATEGORIES))
            buttonsHolder.getChildren().remove(deleteType);
    }

    private void initButtons() {
        addType.setOnMouseClicked((EventHandler<Event>) event -> showTypeDialog(null));
        editType.setOnMouseClicked((EventHandler<Event>) event -> showTypeDialog(selectedType));
        deleteType.setOnMouseClicked((EventHandler<Event>) event -> showDeleteConfirmationDialog());
        editType.setDisable(selectedType == null);
        deleteType.setDisable(selectedType == null);
    }

    private void showTypeDialog(Type type) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.ADD_EXPENSE_TYPE));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            ExpenseTypesDialogController controller = loader.getController();
            controller.initData(type);
            stage.show();
            stage.setOnHidden(e -> typesTable.setItems(ExpenseTypesRepository.getExpenseTypes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDeleteConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete " + selectedType.getName() + "?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == yesButton) {
            boolean successful = ExpenseTypesRepository.deleteExpenseType(selectedType);
            if (successful)
                typesTable.getItems().remove(selectedType);
        } else {
            alert.close();
        }
    }

    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            typesTable.setItems(ExpenseTypesRepository.getExpenseTypesLike(newValue));
        });
    }

    private void initializeTable() {
        TableColumn<Type, String> nameCol = new TableColumn<>("Type");
        typesTable.getColumns().addAll(nameCol);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        typesTable.setItems(ExpenseTypesRepository.getExpenseTypes());
    }

    private void setTableListener() {
        typesTable.selectionModelProperty().get().selectedItemProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
            selectedType = (Type) newValue;
            editType.setDisable(selectedType == null);
            deleteType.setDisable(selectedType == null);
        });
    }
}
