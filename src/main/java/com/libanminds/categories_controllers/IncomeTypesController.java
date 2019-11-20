package com.libanminds.categories_controllers;

import com.libanminds.dialogs_controllers.IncomeTypesDialogController;
import com.libanminds.dialogs_controllers.ItemCategoryDialogController;
import com.libanminds.models.ItemCategory;
import com.libanminds.models.Type;
import com.libanminds.repositories.IncomeTypesRepository;
import com.libanminds.repositories.ItemsCategoriesRepository;
import com.libanminds.utils.Views;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class IncomeTypesController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private Button deleteType;

    @FXML
    private Button editType;

    @FXML
    private Button addType;

    @FXML
    private TableView<Type> typesTable;

    private Type selectedType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtons();
        initSearch();
        initializeTable();
        setTableListener();
    }

    private void initButtons() {
        addType.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                showTypeDialog(null);
            }
        });

        editType.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                showTypeDialog(selectedType);
            }
        });

        deleteType.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                showDeleteConfirmationDialog();
            }
        });

        editType.setDisable(selectedType == null);
        deleteType.setDisable(selectedType == null);
    }

    private void showTypeDialog(Type type) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.ADD_INCOME_TYPE));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            IncomeTypesDialogController controller = loader.getController();
            controller.initData(type);
            stage.show();
            stage.setOnHidden(e -> {
                typesTable.setItems(IncomeTypesRepository.getIncomeTypes());
            });
        }catch (Exception e){}
    }

    private void showDeleteConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete "+ selectedType.getName() + "?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == yesButton) {
            boolean successful = IncomeTypesRepository.deleteIncomeType(selectedType);
            if(successful)
                typesTable.getItems().remove(selectedType);
        } else {
            alert.close();
        }
    }

    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            typesTable.setItems(IncomeTypesRepository.getIncomeTypesLike(newValue));
        });
    }

    private void initializeTable() {
        TableColumn<Type,String> nameCol = new TableColumn<>("Type");
        typesTable.getColumns().addAll(nameCol);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        typesTable.setItems(IncomeTypesRepository.getIncomeTypes());
    }

    private void setTableListener() {
        typesTable.selectionModelProperty().get().selectedItemProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
            selectedType = (Type) newValue;
            editType.setDisable(selectedType == null);
            deleteType.setDisable(selectedType == null);
        });
    }
}
