package com.libanminds.main_controllers;

import com.libanminds.dialogs_controllers.ItemDialogController;
import com.libanminds.models.Item;
import com.libanminds.repositories.ItemsRepository;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ItemsController implements Initializable {

    Item selectedItem;
    @FXML
    private HBox buttonsHolder;
    @FXML
    private TextField searchField;
    @FXML
    private Button deleteItem;
    @FXML
    private Button editItem;
    @FXML
    private Button newItemBtn;
    @FXML
    private MenuItem manageCategories;
    @FXML
    private MenuItem countInventory;
    @FXML
    private MenuButton otherOptions;
    @FXML
    private TableView<Item> itemsTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initButtons();
        initSearch();
        setTableListener();
        handleAuthorization();
    }

    private void handleAuthorization() {
        if (!Authorization.authorized.contains(AuthorizationKeys.CAN_ADD_ITEMS))
            buttonsHolder.getChildren().remove(newItemBtn);

        if (!Authorization.authorized.contains(AuthorizationKeys.CAN_EDIT_ITEMS))
            buttonsHolder.getChildren().remove(editItem);

        if (!Authorization.authorized.contains(AuthorizationKeys.CAN_DELETE_ITEMS))
            buttonsHolder.getChildren().remove(deleteItem);

        if (!Authorization.authorized.contains(AuthorizationKeys.CAN_VIEW_ITEMS_CATEGORIES))
            otherOptions.getItems().remove(manageCategories);

        if (!Authorization.authorized.contains(AuthorizationKeys.CAN_COUNT_ITEMS_INVENTORY))
            otherOptions.getItems().remove(countInventory);

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
            System.out.println(e.getMessage());
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
            stage.setOnHidden(e -> {
                itemsTable.setItems(ItemsRepository.getItems());
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
        TableColumn<Item, String> supplierCol = new TableColumn<>("Supplier");
        TableColumn<Item, String> descriptionCol = new TableColumn<>("Description");
        TableColumn<Item, String> isServiceCol = new TableColumn<>("Is Service");
        TableColumn<Item, String> lastModifierCol = new TableColumn<>("Last Modified");

        itemsTable.getColumns().addAll(imageCol, codeCol, nameCol, categoryCol, priceCol, stockCol, supplierCol, descriptionCol, isServiceCol, lastModifierCol);

        imageCol.setCellValueFactory(new PropertyValueFactory<Item, ImageView>("image"));
        //imageCol.setPrefWidth(60);
        codeCol.setCellValueFactory(new PropertyValueFactory<Item, String>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<Item, String>("category"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Item, String>("initialPrice"));
        stockCol.setCellValueFactory(new PropertyValueFactory<Item, Double>("stock"));
        supplierCol.setCellValueFactory(new PropertyValueFactory<Item, String>("supplier"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Item, String>("description"));
        isServiceCol.setCellValueFactory(new PropertyValueFactory<Item, String>("isService"));
        lastModifierCol.setCellValueFactory(new PropertyValueFactory<Item, String>("lastModified"));

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
