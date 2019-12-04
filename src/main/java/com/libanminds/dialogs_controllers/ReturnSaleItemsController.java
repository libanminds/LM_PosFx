package com.libanminds.dialogs_controllers;

import com.libanminds.models.Item;
import com.libanminds.models.Sale;
import com.libanminds.repositories.ItemsRepository;
import com.libanminds.utils.HelperFunctions;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class ReturnSaleItemsController implements Initializable {


    @FXML
    private TableView<Item> soldItemsTable;

    @FXML
    private TableView<Item> returnedItemsTable;

    @FXML
    private TextField refundAmount;

    private Sale sale;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void setSale(Sale sale) {
        this.sale = sale;
        initializeTables();
    }

    private void initializeTables() {

        //SOLD ITEMS TABLE
        TableColumn<Item, String> codeCol = new TableColumn<>("Code");
        TableColumn<Item, String> nameCol = new TableColumn<>("Name");
        TableColumn<Item, String> saleQuantityCol = new TableColumn<>("Quantity");
        TableColumn<Item, String> priceCol = new TableColumn<>("Price");
        TableColumn<Item, String> saleDiscountCol = new TableColumn<>("Discount");
        TableColumn<Item, String> totalPriceCol = new TableColumn<>("Total");

        soldItemsTable.getColumns().addAll(codeCol, nameCol, saleQuantityCol, priceCol, saleDiscountCol, totalPriceCol);

        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        saleQuantityCol.setCellValueFactory(new PropertyValueFactory<>("saleQuantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("formattedPrice"));
        saleDiscountCol.setCellValueFactory(new PropertyValueFactory<>("formattedDiscount"));
        totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("formattedTotal"));


        soldItemsTable.setRowFactory( tv -> {
            TableRow<Item> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() >= 2 && (! row.isEmpty()) ) {
                    Item item = row.getItem();
                    item.decrementSaleQuantity();
                    soldItemsTable.refresh();
                    returnedItemsTable.getItems().add(item);
                }
            });
            return row ;
        });

        soldItemsTable.setItems(ItemsRepository.getItemsOfSale(sale));

        //RETURNED ITEMS TABLE
        codeCol = new TableColumn<>("Code");
        nameCol = new TableColumn<>("Name");
        saleQuantityCol = new TableColumn<>("Quantity");
        priceCol = new TableColumn<>("Price");
        saleDiscountCol = new TableColumn<>("Discount");
        totalPriceCol = new TableColumn<>("Total");

        returnedItemsTable.getColumns().addAll(codeCol, nameCol, saleQuantityCol, priceCol, saleDiscountCol, totalPriceCol);

        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        saleQuantityCol.setCellValueFactory(new PropertyValueFactory<>("saleQuantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("formattedPrice"));
        saleDiscountCol.setCellValueFactory(new PropertyValueFactory<>("formattedDiscount"));
        totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("formattedTotal"));
    }
}