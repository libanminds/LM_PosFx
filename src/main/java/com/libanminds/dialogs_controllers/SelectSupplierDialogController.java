package com.libanminds.dialogs_controllers;

import com.libanminds.main_controllers.NewReceivingController;
import com.libanminds.models.Supplier;
import com.libanminds.repositories.SupplierRepository;
import com.libanminds.utils.Authorization;
import com.libanminds.utils.AuthorizationKeys;
import com.libanminds.utils.Views;
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
import java.util.ResourceBundle;

public class SelectSupplierDialogController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private Button newSupplierBtn;

    @FXML
    private TableView<Supplier> suppliersTable;

    private Supplier selectedSupplier;

    private NewReceivingController hostController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initSearch();
        initButtons();
        handleAuthorization();
    }

    public void setHostController(NewReceivingController controller) {
        hostController = controller;
    }

    private void handleAuthorization() {
        newSupplierBtn.setVisible(Authorization.authorized.contains(AuthorizationKeys.CAN_ADD_SUPPLIER));
    }

    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            suppliersTable.setItems(SupplierRepository.getSuppliersLike(newValue));
        });
    }

    private void initButtons() {
        newSupplierBtn.setOnMouseClicked((EventHandler<Event>) event -> showSuppliersDialog(null));
    }

    private void showSuppliersDialog(Supplier supplier) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.ADD_SUPPLIER));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            SupplierDialogController controller = loader.getController();
            controller.setHostController(this);
            stage.show();
            stage.setOnHidden(e -> {
                if (selectedSupplier != null) {
                    sendDataBackToHost();
                    Stage currentStage = (Stage) newSupplierBtn.getScene().getWindow();
                    currentStage.close();
                }
            });
        } catch (Exception e) {
        }
    }

    private void initializeTable() {
        TableColumn<Supplier, String> nameCol = new TableColumn<Supplier, String>("Name");
        TableColumn<Supplier, String> companyCol = new TableColumn<Supplier, String>("Company");
        TableColumn<Supplier, String> emailCol = new TableColumn<Supplier, String>("Email");
        TableColumn<Supplier, String> phoneCol = new TableColumn<Supplier, String>("Phone");
        TableColumn<Supplier, String> addressCol = new TableColumn<Supplier, String>("Address");
        TableColumn<Supplier, Double> balanceCol = new TableColumn<Supplier, Double>("Balance");

        suppliersTable.getColumns().addAll(nameCol, companyCol, emailCol, phoneCol, addressCol, balanceCol);

        nameCol.setCellValueFactory(new PropertyValueFactory<Supplier, String>("name"));
        companyCol.setCellValueFactory(new PropertyValueFactory<Supplier, String>("company"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Supplier, String>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<Supplier, String>("phone"));
        addressCol.setCellValueFactory(new PropertyValueFactory<Supplier, String>("address"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<Supplier, Double>("balance"));

        suppliersTable.setRowFactory(tv -> {
            TableRow<Supplier> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() >= 2 && (!row.isEmpty())) {
                    Supplier supplier = row.getItem();
                    setSelectedSupplier(supplier);
                    sendDataBackToHost();
                    Stage currentStage = (Stage) newSupplierBtn.getScene().getWindow();
                    currentStage.close();
                }
            });
            return row;
        });


        suppliersTable.setItems(SupplierRepository.getSuppliers());
    }

    public void setSelectedSupplier(Supplier supplier) {
        selectedSupplier = supplier;
    }

    private void sendDataBackToHost() {
        hostController.setSelectedSupplier(selectedSupplier);
    }
}
