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
                System.out.println("JOKER");
                showCompleteSaleDialog(selectedSale);
            }
        });

        completePayment.setDisable(true);
    }

    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            salesTable.setItems(SalesRepository.getSalesLike(newValue));
        });
    }

    private void showCompleteSaleDialog(Sale sale) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.COMPLETE_SALE));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            CompleteSaleController controller = loader.getController();
            controller.setSale(sale);
            stage.show();
            stage.setOnHidden(e -> {
                salesTable.setItems(SalesRepository.getSales());
            });
        }catch (Exception e){
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
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
        totalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmountFormatted"));
        paidAmount.setCellValueFactory(new PropertyValueFactory<>("paidAmountFormatted"));
        discount.setCellValueFactory(new PropertyValueFactory<>("discountFormatted"));
        remainingAmount.setCellValueFactory(new PropertyValueFactory<>("remainingAmountFormatted"));
        paymentType.setCellValueFactory(new PropertyValueFactory<>("paymentType"));

        salesTable.setItems(SalesRepository.getSales());
    }
    private void setTableListener() {
        salesTable.selectionModelProperty().get().selectedItemProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
            selectedSale = (Sale)newValue;
            completePayment.setDisable(selectedSale == null || selectedSale.isComplete());
        });
    }
}
