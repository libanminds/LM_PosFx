package com.libanminds.main_controllers;

import com.libanminds.dialogs_controllers.CustomerDialogController;
import com.libanminds.dialogs_controllers.SupplierDialogController;
import com.libanminds.models.Customer;
import com.libanminds.models.Supplier;
import com.libanminds.repositories.CustomersRepository;
import com.libanminds.repositories.SupplierRepository;
import com.libanminds.utils.Views;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SuppliersController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private Button deleteSupplier;

    @FXML
    private Button editSupplier;

    @FXML
    private Button newSupplierBtn;

    @FXML
    private TableView<Supplier> suppliersTable;

    Supplier selectedSupplier;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initSearch();
        initButtons();
        setTableListener();
    }

    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            suppliersTable.setItems(SupplierRepository.getSuppliersLike(newValue));
        });
    }

    private void initButtons() {
        newSupplierBtn.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                showSuppliersDialog(null);
            }
        });

        editSupplier.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                showSuppliersDialog(selectedSupplier);
            }
        });

        deleteSupplier.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                showDeleteConfirmationDialog();
            }
        });

        deleteSupplier.setDisable(selectedSupplier == null);
        editSupplier.setDisable(selectedSupplier == null);
    }

    private void showSuppliersDialog(Supplier supplier) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.ADD_SUPPLIER));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            SupplierDialogController controller = loader.getController();
            controller.initData(supplier);
            stage.show();
            stage.setOnHidden(e -> {
                suppliersTable.setItems(SupplierRepository.getSuppliers());
            });
        }catch (Exception e){}
    }

    private void showDeleteConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delele "+ selectedSupplier.getName() + "?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == yesButton) {
            boolean successful = SupplierRepository.deleteSupplier(selectedSupplier);
            if(successful)
                suppliersTable.getItems().remove(selectedSupplier);
        } else {
            alert.close();
        }
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

    private void setTableListener() {
        suppliersTable.selectionModelProperty().get().selectedItemProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
            selectedSupplier = (Supplier) newValue;
            deleteSupplier.setDisable(selectedSupplier == null);
            editSupplier.setDisable(selectedSupplier == null);
        });
    }
}
