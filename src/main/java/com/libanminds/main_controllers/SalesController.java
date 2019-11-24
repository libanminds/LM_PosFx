package com.libanminds.main_controllers;

import com.libanminds.dialogs_controllers.SelectCustomerDialogController;
import com.libanminds.models.Customer;
import com.libanminds.utils.Views;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SalesController implements Initializable {

    @FXML
    private Label customerName;

    @FXML
    private Button selectCustomerBtn;

    private Customer selectedCustomer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtonsClicks();
    }

    private void initButtonsClicks() {
        selectCustomerBtn.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                showSelectCustomerDialog();
            }
        });
    }

    private void showSelectCustomerDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.SELECT_CUSTOMER));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            SelectCustomerDialogController controller = loader.getController();
            controller.setHostController(this);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setSelectedCustomer(Customer customer) {
        selectedCustomer = customer;
        customerName.setText(selectedCustomer == null ? "Guest" : selectedCustomer.getName());
    }
}
