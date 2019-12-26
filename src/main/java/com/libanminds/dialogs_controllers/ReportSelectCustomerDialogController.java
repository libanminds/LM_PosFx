package com.libanminds.dialogs_controllers;

import com.libanminds.models.Customer;
import com.libanminds.models.reports_models.CompactReportItem;
import com.libanminds.repositories.CustomersRepository;
import com.libanminds.repositories.ReportsRepository;
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

public class ReportSelectCustomerDialogController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Customer> customersTable;

    private String dateFrom;
    private String dateTo;
    private ReportType reportType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSearch();
        initializeTable();
    }


    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            customersTable.setItems(CustomersRepository.getCustomersLike(newValue));
        });
    }

    private void initializeTable() {
        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        TableColumn<Customer, String> emailCol = new TableColumn<>("Email");
        TableColumn<Customer, String> phoneCol = new TableColumn<>("Phone");
        TableColumn<Customer, String> addressCol = new TableColumn<>("Address");
        TableColumn<Customer, Double> balanceCol = new TableColumn<>("Balance");

        customersTable.getColumns().addAll(nameCol, emailCol, phoneCol, addressCol, balanceCol);

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));

        customersTable.setRowFactory(tv -> {
            TableRow<Customer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() >= 2 && (!row.isEmpty())) {
                    Customer customer = row.getItem();
                    Stage currentStage = (Stage) searchField.getScene().getWindow();
                    currentStage.close();
                    switch (reportType) {
                        case ITEMS_PER_CUSTOMER:
                            ArrayList<CompactReportItem> itemsPerCustomer = ReportsRepository.getItemsBoughtByCustomer(customer.getID(), dateFrom, dateTo);
                            // generate report
                            break;
                        case CUSTOMER_TRANSACTIONS:
                            // TODO implement
                            // generate report
                            break;
                    }
                }
            });
            return row;
        });
        customersTable.setItems(CustomersRepository.getCustomers());
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
