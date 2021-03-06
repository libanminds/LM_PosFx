package com.libanminds.controllers.main;

import com.libanminds.constants.AuthorizationKeys;
import com.libanminds.constants.Views;
import com.libanminds.controllers.dialogs.ItemDialogController;
import com.libanminds.models.Item;
import com.libanminds.repositories.ItemsRepository;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ItemsController implements Initializable {
    @FXML private HBox buttonsHolder;
    @FXML private TextField searchField;
    @FXML private Button deleteItem;
    @FXML private Button editItem;
    @FXML private Button newItemBtn;
    @FXML private MenuItem manageCategories;
    @FXML private MenuButton otherOptions;
    @FXML private TableView<Item> itemsTable;

    Item selectedItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initButtons();
        initSearch();
        setTableListener();
        handleAuthorization();
    }

    private void handleAuthorization() {
        if (Authorization.cannot(AuthorizationKeys.ADD_ITEMS)) buttonsHolder.getChildren().remove(newItemBtn);
        if (Authorization.cannot(AuthorizationKeys.EDIT_ITEMS)) buttonsHolder.getChildren().remove(editItem);
        if (Authorization.cannot(AuthorizationKeys.DELETE_ITEMS)) buttonsHolder.getChildren().remove(deleteItem);
        if (Authorization.cannot(AuthorizationKeys.VIEW_ITEMS_CATEGORIES))
            otherOptions.getItems().remove(manageCategories);

        otherOptions.setVisible(!otherOptions.getItems().isEmpty());
    }

    private void initButtons() {
        newItemBtn.setOnMouseClicked((EventHandler<Event>) event -> showItemDialog(null));
        editItem.setOnMouseClicked((EventHandler<Event>) event -> showItemDialog(selectedItem));
        deleteItem.setOnMouseClicked((EventHandler<Event>) event -> showDeleteConfirmationDialog());
        manageCategories.setOnAction(t -> showCategoriesDialog());
        deleteItem.setDisable(selectedItem == null);
        editItem.setDisable(selectedItem == null);
    }

    private void showCategoriesDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.ITEMS_CATEGORIES));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showItemDialog(Item item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.ADD_ITEM));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            ItemDialogController controller = loader.getController();
            controller.initData(item);
            stage.show();
            stage.setOnHidden(e -> itemsTable.setItems(ItemsRepository.getItems()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDeleteConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete " + selectedItem.getName());

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == yesButton) {
            boolean successful = ItemsRepository.deleteItem(selectedItem);
            if (successful)
                itemsTable.getItems().remove(selectedItem);
        } else {
            alert.close();
        }
    }

    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            itemsTable.setItems(ItemsRepository.getItemsLike(newValue));
        });
    }

    private void initializeTable() {
        TableColumn<Item, ImageView> imageCol = new TableColumn<>("Image");
        TableColumn<Item, String> codeCol = new TableColumn<>("Code");
        TableColumn<Item, String> nameCol = new TableColumn<>("Name");
        TableColumn<Item, String> categoryCol = new TableColumn<>("Category");
        TableColumn<Item, String> priceCol = new TableColumn<>("Price");
        TableColumn<Item, Double> stockCol = new TableColumn<>("Stock");
        TableColumn<Item, String> descriptionCol = new TableColumn<>("Description");
        TableColumn<Item, String> ttcCol = new TableColumn<>("TTC");

        itemsTable.getColumns().addAll(imageCol, codeCol, nameCol, categoryCol, priceCol, stockCol, descriptionCol, ttcCol);

        imageCol.setCellValueFactory(new PropertyValueFactory<Item, ImageView>("image"));
        codeCol.setCellValueFactory(new PropertyValueFactory<Item, String>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<Item, String>("category"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Item, String>("initialPrice"));
        stockCol.setCellValueFactory(new PropertyValueFactory<Item, Double>("stock"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Item, String>("description"));
        ttcCol.setCellValueFactory(new PropertyValueFactory<Item, String>("priceIncludesTax"));

        itemsTable.setItems(ItemsRepository.getItems());
    }

    private void setTableListener() {
        itemsTable.selectionModelProperty().get().selectedItemProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
            selectedItem = (Item) newValue;
            deleteItem.setDisable(selectedItem == null);
            editItem.setDisable(selectedItem == null);
        });
    }
}
