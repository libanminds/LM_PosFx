package com.libanminds.controllers.dialogs;

import com.libanminds.constants.AuthorizationKeys;
import com.libanminds.constants.Views;
import com.libanminds.controllers.main.NewPurchaseController;
import com.libanminds.controllers.main.NewSaleController;
import com.libanminds.models.Item;
import com.libanminds.repositories.ItemsRepository;
import com.libanminds.utils.Authorization;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SelectItemDialogController implements Initializable {
    @FXML private TextField searchField;
    @FXML private Button newItemBtn;
    @FXML private TableView<Item> itemsTable;

    private Item selectedItem;

    private NewSaleController hostControllerSale;
    private NewPurchaseController hostControllerPurchase;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initButtons();
        initSearch();
        handleAuthorization();
    }

    public void setHostController(NewSaleController controllerSales, NewPurchaseController controllerPurchases) {
        hostControllerSale = controllerSales;
        hostControllerPurchase = controllerPurchases;
    }

    private void handleAuthorization() {
        newItemBtn.setVisible(Authorization.can(AuthorizationKeys.ADD_ITEMS));
    }

    private void initButtons() {
        newItemBtn.setOnMouseClicked((EventHandler<Event>) event -> showItemDialog());
    }

    private void showItemDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.ADD_ITEM));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            ItemDialogController controller = loader.getController();
            controller.setHostController(this);
            selectedItem = null;
            stage.show();
            stage.setOnHidden(e -> {
                if (selectedItem != null) {
                    sendDataBackToHost();
                    Stage currentStage = (Stage) newItemBtn.getScene().getWindow();
                    currentStage.close();
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
        priceCol.setCellValueFactory(new PropertyValueFactory<Item, String>("price"));
        stockCol.setCellValueFactory(new PropertyValueFactory<Item, Double>("stock"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Item, String>("description"));
        ttcCol.setCellValueFactory(new PropertyValueFactory<Item, String>("priceIncludesTax"));

        itemsTable.setRowFactory(tv -> {
            TableRow<Item> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() >= 2 && (!row.isEmpty())) {
                    Item item = row.getItem();
                    setSelectedItem(item);
                    sendDataBackToHost();
                    Stage currentStage = (Stage) newItemBtn.getScene().getWindow();
                    currentStage.close();
                }
            });
            return row;
        });

        itemsTable.setItems(ItemsRepository.getItems());
    }

    private void sendDataBackToHost() {
        if (hostControllerSale != null)
            hostControllerSale.setSelectedItem(selectedItem);

        if (hostControllerPurchase != null)
            hostControllerPurchase.setSelectedItem(selectedItem);
    }

    public void setSelectedItem(Item item) {
        selectedItem = item;
    }
}
