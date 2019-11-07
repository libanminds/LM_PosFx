package com.libanminds.main_controllers;

import com.libanminds.models.Customer;
import com.libanminds.repositories.CustomersRepository;
import com.libanminds.utils.Views;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomersController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private Button newCustomerBtn;

    @FXML
    private TableView<Customer> customersTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initNewCustomer();
        initSearch();
        initializeTable();
    }

    private void initNewCustomer() {
        newCustomerBtn.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                try{
                    Parent root;
                    Stage primaryStage = new Stage();
                    root = FXMLLoader.load(getClass().getResource(Views.ADD_CUSTOMER));
                    Scene scene = new Scene(root);
                    primaryStage.initModality(Modality.APPLICATION_MODAL);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                }
                catch(Exception e){
                    System.out.println(e.getMessage());
                };
            }
        });
    }

    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            customersTable.setItems(CustomersRepository.getCustomersLike(newValue));
        });
    }

    private void initializeTable() {
        TableColumn<Customer,String> nameCol = new TableColumn<Customer,String>("Name");
        TableColumn<Customer,String> emailCol = new TableColumn<Customer,String>("Email");
        TableColumn<Customer,String> phoneCol = new TableColumn<Customer,String>("Phone");
        TableColumn<Customer,String> addressCol = new TableColumn<Customer,String>("Address");
        TableColumn<Customer,Double> balanceCol = new TableColumn<Customer,Double>("Balance");

        customersTable.getColumns().addAll(nameCol,emailCol,phoneCol,addressCol,balanceCol);

        nameCol.setCellValueFactory(new PropertyValueFactory<Customer,String>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Customer,String>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<Customer,String>("phone"));
        addressCol.setCellValueFactory(new PropertyValueFactory<Customer,String>("address"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<Customer,Double>("balance"));

        customersTable.setItems(CustomersRepository.getCustomers());
    }
}
