package com.libanminds.main_controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.libanminds.models.*;
import com.libanminds.models.reports_models.CompactReportItem;
import com.libanminds.repositories.ReportsRepository;
import com.libanminds.utils.Constants;
import com.libanminds.utils.ReportType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    @FXML
    private JFXDatePicker dateFrom;

    @FXML
    private JFXDatePicker dateTo;

    @FXML
    private JFXButton salesBtn;

    @FXML
    private JFXButton purchasesBtn;

    @FXML
    private JFXButton customersBtn;

    @FXML
    private JFXButton suppliersBtn;

    @FXML
    private JFXButton itemsSoldBtn;

    @FXML
    private JFXButton itemsPurchasedBtn;

    @FXML
    private JFXButton expensesBtn;

    @FXML
    private JFXButton incomeBtn;

    private Customer selectedCustomer;
    private Supplier selectedSupplier;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtons();
        initDates();
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    public void setSelectedSupplier(Supplier selectedSupplier) {
        this.selectedSupplier = selectedSupplier;
    }

    private void initButtons() {
        salesBtn.setOnMouseClicked(event -> generateSalesReport());
        purchasesBtn.setOnMouseClicked(event -> generatePurchasesReport());
        customersBtn.setOnMouseClicked(event -> showCustomersAdvancedOptions());
        suppliersBtn.setOnMouseClicked(event -> showSuppliersAdvancedOptions());
        itemsSoldBtn.setOnMouseClicked(event -> generateItemsSoldReport());
        itemsPurchasedBtn.setOnMouseClicked(event -> generateItemsPurchasedReport());
        expensesBtn.setOnMouseClicked(event -> generateExpensesReport());
        incomeBtn.setOnMouseClicked(event -> generateIncomeReport());
    }

    private void initDates() {
        LocalDate today = LocalDate.now();
        dateFrom.setValue(today.minusMonths(1));
        dateTo.setValue(today);
    }

    private void showCustomersAdvancedOptions() {

    }

    private void showSuppliersAdvancedOptions() {

    }


    private void generateItemsSoldReport() {
        ArrayList<CompactReportItem> itemsSold = ReportsRepository.getQuantitiesOfSoldItems(
                dateFrom.getValue().format(Constants.REPORT_DATE_FORMAT),
                dateTo.getValue().format(Constants.REPORT_DATE_FORMAT)
        );
    }

    private void generateItemsPurchasedReport() {
        ArrayList<CompactReportItem> itemsPurchased = ReportsRepository.getQuantitiesOfPurchasedItems(
                dateFrom.getValue().format(Constants.REPORT_DATE_FORMAT),
                dateTo.getValue().format(Constants.REPORT_DATE_FORMAT)
        );
    }

    private void generateSalesReport() {
        ArrayList<Sale> sales = ReportsRepository.getInfoOfSales(
                dateFrom.getValue().format(Constants.REPORT_DATE_FORMAT),
                dateTo.getValue().format(Constants.REPORT_DATE_FORMAT)
        );
    }

    private void generatePurchasesReport() {
        ArrayList<Receiving> purchases = ReportsRepository.getInfoOfPurchases(
                dateFrom.getValue().format(Constants.REPORT_DATE_FORMAT),
                dateTo.getValue().format(Constants.REPORT_DATE_FORMAT)
        );
    }

    private void generateIncomeReport() {
        ArrayList<Income> incomes = ReportsRepository.getIncomes(
                dateFrom.getValue().format(Constants.REPORT_DATE_FORMAT),
                dateTo.getValue().format(Constants.REPORT_DATE_FORMAT)
        );
    }

    private void generateExpensesReport() {
        ArrayList<Expense> expenses = ReportsRepository.getExpenses(
                dateFrom.getValue().format(Constants.REPORT_DATE_FORMAT),
                dateTo.getValue().format(Constants.REPORT_DATE_FORMAT)
        );
    }

    private void generateCustomersReport() {
        // if sorted by date
        ArrayList<Customer> customers = ReportsRepository.getCustomers(
                dateFrom.getValue().format(Constants.REPORT_DATE_FORMAT),
                dateTo.getValue().format(Constants.REPORT_DATE_FORMAT)
        );
        // else if sorted by top sales
        ArrayList<Customer> regulars = ReportsRepository.getRegularCustomers(
                dateFrom.getValue().format(Constants.REPORT_DATE_FORMAT),
                dateTo.getValue().format(Constants.REPORT_DATE_FORMAT)
        );
    }

    private void generateSuppliersReport() {
        ArrayList<Supplier> suppliers = ReportsRepository.getSuppliers(
                dateFrom.getValue().format(Constants.REPORT_DATE_FORMAT),
                dateTo.getValue().format(Constants.REPORT_DATE_FORMAT)
        );
    }
}
