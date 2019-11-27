package com.libanminds.dialogs_controllers;

import com.libanminds.main_controllers.ReceivingController;
import com.libanminds.main_controllers.SalesController;
import com.libanminds.models.Customer;
import com.libanminds.models.Item;
import com.libanminds.repositories.ItemsRepository;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SelectItemDialogController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private Button newItemBtn;

    @FXML
    private TableView<Item> itemsTable;

    private Item selectedItem;

    private SalesController hostControllerSale;
    private ReceivingController hostControllerReceiving;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initButtons();
        initSearch();
    }

    public void setHostController (SalesController controllerSales, ReceivingController controllerReceivings) {
        hostControllerSale = controllerSales;
        hostControllerReceiving = controllerReceivings;
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
                if(selectedItem != null) {
                    sendDataBackToHost();
                    Stage currentStage = (Stage) newItemBtn.getScene().getWindow();
                    currentStage.close();
                }
            });
        }catch (Exception e){
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
        TableColumn<Item,String> codeCol = new TableColumn<>("Code");
        TableColumn<Item,String> nameCol = new TableColumn<>("Name");
        TableColumn<Item,String> categoryCol = new TableColumn<>("Category");
        TableColumn<Item,String> priceCol = new TableColumn<>("Price");
        TableColumn<Item,Double> stockCol = new TableColumn<>("Stock");
        TableColumn<Item,String> supplierCol = new TableColumn<>("Supplier");
        TableColumn<Item,String> descriptionCol = new TableColumn<>("Description");
        TableColumn<Item,String> isServiceCol = new TableColumn<>("Is Service");
        TableColumn<Item,String> lastModifierCol = new TableColumn<>("Last Modified");

        itemsTable.getColumns().addAll(imageCol,codeCol,nameCol,categoryCol,priceCol,stockCol,supplierCol,descriptionCol,isServiceCol,lastModifierCol);

        imageCol.setCellValueFactory(new PropertyValueFactory<Item,ImageView>("image"));
        codeCol.setCellValueFactory(new PropertyValueFactory<Item,String>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Item,String>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<Item,String>("category"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Item,String>("price"));
        stockCol.setCellValueFactory(new PropertyValueFactory<Item,Double>("stock"));
        supplierCol.setCellValueFactory(new PropertyValueFactory<Item,String>("supplier"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Item,String>("description"));
        isServiceCol.setCellValueFactory(new PropertyValueFactory<Item,String>("isService"));
        lastModifierCol.setCellValueFactory(new PropertyValueFactory<Item,String>("lastModified"));

        itemsTable.setRowFactory( tv -> {
            TableRow<Item> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() >= 2 && (! row.isEmpty()) ) {
                    Item item = row.getItem();
                    setSelectedItem(item);
                    sendDataBackToHost();
                    Stage currentStage = (Stage) newItemBtn.getScene().getWindow();
                    currentStage.close();
                }
            });
            return row ;
        });

        itemsTable.setItems(ItemsRepository.getItems());
    }

    private void sendDataBackToHost() {
        if(hostControllerSale != null)
            hostControllerSale.setSelectedItem(selectedItem);

        if(hostControllerReceiving != null)
            hostControllerReceiving.setSelectedItem(selectedItem);
    }

    public void setSelectedItem(Item item) {
        selectedItem = item;
    }
}
