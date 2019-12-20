package com.libanminds.main_controllers;

import com.libanminds.dialogs_controllers.*;
import com.libanminds.models.Receiving;
import com.libanminds.repositories.ReceivingsRepository;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ReceivingsController implements Initializable {

    @FXML
    private HBox buttonsHolder;

    @FXML
    private TextField searchField;

    @FXML
    private Button completePayment;

    @FXML
    private Button viewReceiving;

    @FXML
    private Button returnItems;

    @FXML
    private TableView<Receiving> receivingsTable;

    private Receiving selectedReceiving;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtons();
        initSearch();
        initializeTable();
        setTableListener();
        handleAuthorization();
    }

    private void handleAuthorization() {
        if(!Authorization.authorized.contains(AuthorizationKeys.CAN_VIEW_RECEIVING))
            buttonsHolder.getChildren().remove(viewReceiving);

        if(!Authorization.authorized.contains(AuthorizationKeys.CAN_RETURN_RECEIVING_ITEMS))
            buttonsHolder.getChildren().remove(returnItems);

        if(!Authorization.authorized.contains(AuthorizationKeys.CAN_COMPLETE_RECEIVING_PAYMENT))
            buttonsHolder.getChildren().remove(completePayment);
    }

    private void initButtons() {
        completePayment.setOnMouseClicked((EventHandler<Event>) event -> {
            showCompleteReceivingDialog(selectedReceiving);
        });

        viewReceiving.setOnMouseClicked((EventHandler<Event>) event -> {
            showViewReceivingDialog(selectedReceiving);
        });

        returnItems.setOnMouseClicked((EventHandler<Event>) event -> {
            showReturnItemsDialog(selectedReceiving);
        });
        viewReceiving.setDisable(true);
        completePayment.setDisable(true);
        returnItems.setDisable(true);
    }

    private void showViewReceivingDialog(Receiving receiving) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.VIEW_RECEIVING));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            ViewReceivingController controller = loader.getController();
            controller.setReceiving(receiving);
            stage.show();
            stage.setOnHidden(e -> {
                receivingsTable.setItems(ReceivingsRepository.getReceivings());
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showReturnItemsDialog(Receiving receiving) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.RETURN_RECEIVING_ITEMS));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            ReturnReceivingItemsController controller = loader.getController();
            controller.setReceiving(receiving);
            stage.show();
            stage.setOnHidden(e -> {
                receivingsTable.setItems(ReceivingsRepository.getReceivings());
            });
        }catch (Exception e){
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void showCompleteReceivingDialog(Receiving receiving) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.COMPLETE_RECEIVING));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            CompleteReceivingController controller = loader.getController();
            controller.setReceiving(receiving);
            stage.show();
            stage.setOnHidden(e -> {
                receivingsTable.setItems(ReceivingsRepository.getReceivings());
            });
        }catch (Exception e){

            e.printStackTrace();
        }
    }

    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            receivingsTable.setItems(ReceivingsRepository.getReceivingsLike(newValue));
        });
    }

    private void initializeTable() {
        TableColumn<Receiving,String> customerName = new TableColumn<>("Supplier Name");
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
            viewReceiving.setDisable(selectedReceiving == null);
            returnItems.setDisable(selectedReceiving == null);
        });
    }
}
