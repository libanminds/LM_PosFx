package com.libanminds.controllers.dialogs;

import com.libanminds.constants.AuthorizationKeys;
import com.libanminds.constants.Views;
import com.libanminds.controllers.main.NewPurchaseController;
import com.libanminds.controllers.reports.SuppliersReportController;
import com.libanminds.models.Supplier;
import com.libanminds.repositories.SupplierRepository;
import com.libanminds.utils.Authorization;
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
    @FXML private TextField searchField;
    @FXML private Button newSupplierBtn;
    @FXML private TableView<Supplier> suppliersTable;

    private Supplier selectedSupplier;
    private NewPurchaseController purchaseController;
    private SuppliersReportController reportsController;

    private boolean fromReports;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initSearch();
        initButtons();
        handleAuthorization();
        fromReports = false;
    }

    public void setPurchaseController(NewPurchaseController controller) {
        purchaseController = controller;
    }

    private void handleAuthorization() {
        newSupplierBtn.setVisible(Authorization.can(AuthorizationKeys.ADD_SUPPLIER));
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
            e.printStackTrace();
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
                    Stage currentStage = (Stage) searchField.getScene().getWindow();
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
        if (fromReports) {
            reportsController.setSelectedSupplier(selectedSupplier);
        } else {
            purchaseController.setSelectedSupplier(selectedSupplier);
        }
    }

    public void setFromReports(boolean fromReports) {
        this.fromReports = fromReports;
        newSupplierBtn.setVisible(!fromReports);
    }

    public void setReportsController(SuppliersReportController reportsController) {
        this.reportsController = reportsController;
    }
}
