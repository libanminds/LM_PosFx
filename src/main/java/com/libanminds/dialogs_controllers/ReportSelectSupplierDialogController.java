package com.libanminds.dialogs_controllers;

import com.libanminds.models.Supplier;
import com.libanminds.models.reports_models.CompactReportItem;
import com.libanminds.repositories.ReportsRepository;
import com.libanminds.repositories.SupplierRepository;
import com.libanminds.utils.ReportType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ReportSelectSupplierDialogController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Supplier> suppliersTable;

    private String dateFrom;
    private String dateTo;
    private ReportType reportType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initSearch();
    }


    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            suppliersTable.setItems(SupplierRepository.getSuppliersLike(newValue));
        });
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
                    Stage currentStage = (Stage) searchField.getScene().getWindow();
                    currentStage.close();
                    switch (reportType) {
                        case ITEMS_PER_SUPPLIER:
                            ArrayList<CompactReportItem> itemsPerCustomer = ReportsRepository.getItemsBoughtFromSupplier(supplier.getID(), dateFrom, dateTo);
                            // generate report
                            break;
                        case SUPPLIER_TRANSACTIONS:
                            // TODO implement
                            // generate report
                            break;
                    }
                }
            });
            return row;
        });
        suppliersTable.setItems(SupplierRepository.getSuppliers());
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }
}
