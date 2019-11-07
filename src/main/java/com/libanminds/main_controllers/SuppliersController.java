package com.libanminds.main_controllers;

import com.libanminds.models.Supplier;
import com.libanminds.repositories.CustomersRepository;
import com.libanminds.repositories.SupplierRepository;
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

public class SuppliersController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private Button newSupplierBtn;

    @FXML
    private TableView<Supplier> suppliersTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initSearch();
        initNewSupplier();
    }

    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            suppliersTable.setItems(SupplierRepository.getSuppliersLike(newValue));
        });
    }

    private void initNewSupplier() {
        newSupplierBtn.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                try{
                    Parent root;
                    Stage addCustomerStage = new Stage();
                    root = FXMLLoader.load(getClass().getResource(Views.ADD_SUPPLIER));
                    Scene scene = new Scene(root);
                    addCustomerStage.initModality(Modality.APPLICATION_MODAL);
                    addCustomerStage.setScene(scene);
                    addCustomerStage.show();
                    addCustomerStage.setOnHidden(e -> {
                        suppliersTable.setItems(SupplierRepository.getSuppliers());
                    });
                }
                catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    private void initializeTable() {
        TableColumn<Supplier,String> nameCol = new TableColumn<Supplier,String>("Name");
        TableColumn<Supplier,String> companyCol = new TableColumn<Supplier,String>("Company");
        TableColumn<Supplier,String> emailCol = new TableColumn<Supplier,String>("Email");
        TableColumn<Supplier,String> phoneCol = new TableColumn<Supplier,String>("Phone");
        TableColumn<Supplier,String> addressCol = new TableColumn<Supplier,String>("Address");
        TableColumn<Supplier,Double> balanceCol = new TableColumn<Supplier,Double>("Balance");

        suppliersTable.getColumns().addAll(nameCol,companyCol,emailCol,phoneCol,addressCol,balanceCol);

        nameCol.setCellValueFactory(new PropertyValueFactory<Supplier,String>("name"));
        companyCol.setCellValueFactory(new PropertyValueFactory<Supplier,String>("company"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Supplier,String>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<Supplier,String>("phone"));
        addressCol.setCellValueFactory(new PropertyValueFactory<Supplier,String>("address"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<Supplier,Double>("balance"));

        suppliersTable.setItems(SupplierRepository.getSuppliers());
    }
}
