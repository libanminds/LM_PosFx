package com.libanminds.main_controllers;

import com.libanminds.models.Item;
import com.libanminds.repositories.ItemsRepository;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ItemsController implements Initializable {
    @FXML
    private TextField searchField;

    @FXML
    private Button newItemBtn;

    @FXML
    private TableView<Item> itemsTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
    }

    private void initializeTable() {
        TableColumn<Item,String> imageCol = new TableColumn<Item,String>("Image");
        TableColumn<Item,String> codeCol = new TableColumn<Item,String>("Code");
        TableColumn<Item,String> nameCol = new TableColumn<Item,String>("Name");
        TableColumn<Item,String> categoryCol = new TableColumn<Item,String>("Category");
        TableColumn<Item,String> priceCol = new TableColumn<Item,String>("Price");
        TableColumn<Item,Double> stockCol = new TableColumn<Item,Double>("Stock");
        TableColumn<Item,String> supplierCol = new TableColumn<Item,String>("Supplier");
        TableColumn<Item,String> descriptionCol = new TableColumn<Item,String>("Description");
        TableColumn<Item,String> priceIncludesTaxCol = new TableColumn<Item,String>("Price includes Tax");
        TableColumn<Item,String> isServiceCol = new TableColumn<Item,String>("Is Service");
        TableColumn<Item,String> lastModifierCol = new TableColumn<Item,String>("Last Modified");

        itemsTable.getColumns().addAll(imageCol,codeCol,nameCol,categoryCol,priceCol,stockCol,supplierCol,descriptionCol,priceIncludesTaxCol,isServiceCol,lastModifierCol);

        imageCol.setCellValueFactory(new PropertyValueFactory<Item,String>("imageUrl"));
        codeCol.setCellValueFactory(new PropertyValueFactory<Item,String>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Item,String>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<Item,String>("category"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Item,String>("price"));
        stockCol.setCellValueFactory(new PropertyValueFactory<Item,Double>("stock"));
        supplierCol.setCellValueFactory(new PropertyValueFactory<Item,String>("supplier"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Item,String>("description"));
        priceIncludesTaxCol.setCellValueFactory(new PropertyValueFactory<Item,String>("priceIncludingTax"));
        isServiceCol.setCellValueFactory(new PropertyValueFactory<Item,String>("isService"));
        lastModifierCol.setCellValueFactory(new PropertyValueFactory<Item,String>("lastModified"));

        itemsTable.setItems(ItemsRepository.getItems());
    }
}
