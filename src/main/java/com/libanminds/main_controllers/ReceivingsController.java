package com.libanminds.main_controllers;

import com.libanminds.models.Receiving;
import com.libanminds.models.Sale;
import com.libanminds.repositories.ReceivingsRepository;
import com.libanminds.repositories.SalesRepository;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ReceivingsController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private Button completePayment;

    @FXML
    private TableView<Receiving> receivingsTable;

    private Receiving selectedReceiving;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtons();
        initSearch();
        initializeTable();
        setTableListener();
    }

    private void initButtons() {
        completePayment.setOnMouseClicked((EventHandler<Event>) event -> {
            //TODO: Show some dialog
        });

        completePayment.setDisable(true);
    }

    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            receivingsTable.setItems(ReceivingsRepository.getReceivingsLike(newValue));
        });
    }

    private void initializeTable() {
        TableColumn<Receiving,String> customerName = new TableColumn<>("Customer Name");
        TableColumn<Receiving,String> totalAmount = new TableColumn<>("Total Amount");
        TableColumn<Receiving,String> paidAmount = new TableColumn<>("Paid Amount");
        TableColumn<Receiving,String> discount = new TableColumn<>("Discount");
        TableColumn<Receiving,String> remainingAmount = new TableColumn<>("Remaining Amount");
        TableColumn<Receiving,String> paymentType = new TableColumn<>("Payment Type");

        receivingsTable.getColumns().addAll(customerName,totalAmount,paidAmount,discount,remainingAmount,paymentType);

        customerName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        totalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmountFormatted"));
        paidAmount.setCellValueFactory(new PropertyValueFactory<>("paidAmountFormatted"));
        discount.setCellValueFactory(new PropertyValueFactory<>("discountFormatted"));
        remainingAmount.setCellValueFactory(new PropertyValueFactory<>("remainingAmountFormatted"));
        paymentType.setCellValueFactory(new PropertyValueFactory<>("paymentType"));

        receivingsTable.setItems(ReceivingsRepository.getReceivings());
    }

    private void setTableListener() {
        receivingsTable.selectionModelProperty().get().selectedItemProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
            selectedReceiving = (Receiving) newValue;
            completePayment.setDisable(selectedReceiving == null || selectedReceiving.isComplete());
        });
    }
}
