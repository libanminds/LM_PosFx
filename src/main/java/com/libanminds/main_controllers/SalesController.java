package com.libanminds.main_controllers;

import com.libanminds.dialogs_controllers.CustomerDialogController;
import com.libanminds.models.Customer;
import com.libanminds.models.Sale;
import com.libanminds.repositories.CustomersRepository;
import com.libanminds.repositories.SalesRepository;
import com.libanminds.utils.Views;
import com.sun.javafx.menu.MenuItemBase;
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

public class SalesController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private Button completePayment;

    @FXML
    private TableView<Sale> salesTable;

    private Sale selectedSale;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtons();
        initSearch();
        initializeTable();
        setTableListener();
    }

    private void initButtons() {
        completePayment.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                //TODO: Show some dialog
            }
        });

        completePayment.setDisable(true);
    }

    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            salesTable.setItems(SalesRepository.getSalesLike(newValue));
        });
    }

    private void initializeTable() {
        TableColumn<Sale,String> customerName = new TableColumn<>("Customer Name");
        TableColumn<Sale,String> totalAmount = new TableColumn<>("Total Amount");
        TableColumn<Sale,String> paidAmount = new TableColumn<>("Paid Amount");
        TableColumn<Sale,String> discount = new TableColumn<>("Discount");
        TableColumn<Sale,String> remainingAmount = new TableColumn<>("Remaining Amount");
        TableColumn<Sale,String> paymentType = new TableColumn<>("Payment Type");

        salesTable.getColumns().addAll(customerName,totalAmount,paidAmount,discount,remainingAmount,paymentType);

        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        totalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        paidAmount.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));
        discount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        remainingAmount.setCellValueFactory(new PropertyValueFactory<>("remainingAmountFormatted"));
        paymentType.setCellValueFactory(new PropertyValueFactory<>("paymentType"));

        salesTable.setItems(SalesRepository.getSales());
    }

    private void setTableListener() {
        salesTable.selectionModelProperty().get().selectedItemProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
            selectedSale = (Sale)newValue;
            completePayment.setDisable(selectedSale == null && selectedSale.isComplete());
        });
    }
}
