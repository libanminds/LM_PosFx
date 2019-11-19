package com.libanminds.main_controllers;

import com.libanminds.dialogs_controllers.ItemDialogController;
import com.libanminds.models.Item;
import com.libanminds.repositories.ItemsRepository;
import com.libanminds.utils.Views;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ItemsController implements Initializable {
    @FXML
    private TextField searchField;

    @FXML
    private Button deleteItem;

    @FXML
    private Button editItem;

    @FXML
    private Button newItemBtn;

    @FXML
    private TableView<Item> itemsTable;

    Item selectedItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initButtons();
        initSearch();
        setTableListener();
    }

    private void initButtons() {
        newItemBtn.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                showItemDialog(null);
            }
        });

        editItem.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                showItemDialog(selectedItem);
            }
        });

        deleteItem.setDisable(selectedItem == null);
        editItem.setDisable(selectedItem == null);
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
        }catch (Exception e){}
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
        //imageCol.setPrefWidth(60);
        codeCol.setCellValueFactory(new PropertyValueFactory<Item,String>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Item,String>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<Item,String>("category"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Item,String>("price"));
        stockCol.setCellValueFactory(new PropertyValueFactory<Item,Double>("stock"));
        supplierCol.setCellValueFactory(new PropertyValueFactory<Item,String>("supplier"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Item,String>("description"));
        isServiceCol.setCellValueFactory(new PropertyValueFactory<Item,String>("isService"));
        lastModifierCol.setCellValueFactory(new PropertyValueFactory<Item,String>("lastModified"));

        itemsTable.setItems(ItemsRepository.getItems());
    }

    private void setTableListener() {
        itemsTable.selectionModelProperty().get().selectedItemProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
            selectedItem = (Item)newValue;
            deleteItem.setDisable(selectedItem == null);
            editItem.setDisable(selectedItem == null);
        });
    }
}
