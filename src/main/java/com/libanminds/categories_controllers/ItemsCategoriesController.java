package com.libanminds.categories_controllers;

import com.libanminds.dialogs_controllers.ItemCategoryDialogController;
import com.libanminds.models.ItemCategory;
import com.libanminds.repositories.ItemsCategoriesRepository;
import com.libanminds.utils.Authorization;
import com.libanminds.utils.AuthorizationKeys;
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
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ItemsCategoriesController implements Initializable {

    @FXML
    private HBox buttonsHolder;

    @FXML
    private TextField searchField;

    @FXML
    private Button deleteCategory;

    @FXML
    private Button editCategory;

    @FXML
    private Button addCategory;

    @FXML
    private TableView<ItemCategory> categoriesTable;

    private ItemCategory selectedCategory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtons();
        initSearch();
        initializeTable();
        setTableListener();
        handleAuthorization();
    }

    private void handleAuthorization() {
        if(!Authorization.authorized.contains(AuthorizationKeys.CAN_ADD_ITEMS_CATEGORIES))
            buttonsHolder.getChildren().remove(addCategory);

        if(!Authorization.authorized.contains(AuthorizationKeys.CAN_EDIT_ITEMS_CATEGORIES))
            buttonsHolder.getChildren().remove(editCategory);

        if(!Authorization.authorized.contains(AuthorizationKeys.CAN_DELETE_ITEMS_CATEGORIES))
            buttonsHolder.getChildren().remove(deleteCategory);
    }

    private void initButtons() {
        addCategory.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                showItemCategoryDialog(null);
            }
        });

        editCategory.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                showItemCategoryDialog(selectedCategory);
            }
        });

        deleteCategory.setOnMouseClicked((EventHandler<Event>) event -> showDeleteConfirmationDialog());

        editCategory.setDisable(selectedCategory == null);
        deleteCategory.setDisable(selectedCategory == null);
    }

    private void showItemCategoryDialog(ItemCategory category) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.ADD_ITEM_CATEGORY));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            ItemCategoryDialogController controller = loader.getController();
            controller.initData(category);
            stage.show();
            stage.setOnHidden(e -> {
                categoriesTable.setItems(ItemsCategoriesRepository.getItemsCategories());
            });
        }catch (Exception e){}
    }

    private void showDeleteConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete "+ selectedCategory.getName() + "?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == yesButton) {
            boolean successful = ItemsCategoriesRepository.deleteItemCategory(selectedCategory);
            if(successful)
                categoriesTable.getItems().remove(selectedCategory);
        } else {
            alert.close();
        }
    }

    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            categoriesTable.setItems(ItemsCategoriesRepository.getItemsCategoriesLike(newValue));
        });
    }

    private void initializeTable() {
        TableColumn<ItemCategory,String> nameCol = new TableColumn<>("Category");
        categoriesTable.getColumns().addAll(nameCol);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoriesTable.setItems(ItemsCategoriesRepository.getItemsCategories());
    }

    private void setTableListener() {
        categoriesTable.selectionModelProperty().get().selectedItemProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
            selectedCategory = (ItemCategory) newValue;
            editCategory.setDisable(selectedCategory == null);
            deleteCategory.setDisable(selectedCategory == null);
        });
    }
}
