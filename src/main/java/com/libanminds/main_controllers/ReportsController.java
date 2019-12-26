package com.libanminds.main_controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.libanminds.dialogs_controllers.ReportSelectCustomerDialogController;
import com.libanminds.dialogs_controllers.ReportSelectSupplierDialogController;
import com.libanminds.models.*;
import com.libanminds.models.reports_models.CompactReportItem;
import com.libanminds.repositories.ReportsRepository;
import com.libanminds.utils.Constants;
import com.libanminds.utils.ReportType;
import com.libanminds.utils.Views;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    private JFXButton expensesBtn;

    @FXML
    private JFXButton itemsSoldBtn;

    @FXML
    private JFXButton itemsPurchasedBtn;

    @FXML
    private JFXButton salesBtn;

    @FXML
    private JFXButton purchasesBtn;

    @FXML
    private JFXButton customersBtn;

    @FXML
    private JFXButton customerTransactionsBtn;

    @FXML
    private JFXButton itemsPerCustomerBtn;

    @FXML
    private JFXButton suppliersBtn;

    @FXML
    private JFXButton supplierTransactionsBtn;

    @FXML
    private JFXButton itemsPerSupplierBtn;

    @FXML
    private JFXButton incomeBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtons();
        initDates();
    }

    private void initButtons() {
        itemsPurchasedBtn.setOnMouseClicked(event -> generateReport(ReportType.ITEMS_PURCHASED));
        itemsSoldBtn.setOnMouseClicked(event -> generateReport(ReportType.ITEMS_SOLD));
        salesBtn.setOnMouseClicked(event -> generateReport(ReportType.SALES));
        purchasesBtn.setOnMouseClicked(event -> generateReport(ReportType.PURCHASES));
        customersBtn.setOnMouseClicked(event -> generateReport(ReportType.CUSTOMERS));
        customerTransactionsBtn.setOnMouseClicked(event -> selectCustomer(ReportType.CUSTOMER_TRANSACTIONS));
        itemsPerCustomerBtn.setOnMouseClicked(event -> selectCustomer(ReportType.ITEMS_PER_CUSTOMER));
        suppliersBtn.setOnMouseClicked(event -> generateReport(ReportType.SUPPLIERS));
        supplierTransactionsBtn.setOnMouseClicked(event -> selectSupplier(ReportType.SUPPLIER_TRANSACTIONS));
        itemsPerSupplierBtn.setOnMouseClicked(event -> selectSupplier(ReportType.ITEMS_PER_SUPPLIER));
        incomeBtn.setOnMouseClicked(event -> generateReport(ReportType.INCOME));
        expensesBtn.setOnMouseClicked(event -> generateReport(ReportType.EXPENSES));
    }

    private void initDates() {
        LocalDate today = LocalDate.now();
        dateFrom.setValue(today.minusMonths(1));
        dateTo.setValue(today);
    }

    private void selectCustomer(ReportType reportAfterSelection) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.REPORT_SELECT_CUSTOMER));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            ReportSelectCustomerDialogController controller = loader.getController();
            controller.setDateFrom(dateFrom.getValue().format(Constants.REPORT_DATE_FORMAT));
            controller.setDateTo(dateTo.getValue().format(Constants.REPORT_DATE_FORMAT));
            controller.setReportType(reportAfterSelection);
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void selectSupplier(ReportType reportAfterSelection) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.REPORT_SELECT_SUPPLIER));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            ReportSelectSupplierDialogController controller = loader.getController();
            controller.setDateFrom(dateFrom.getValue().format(Constants.REPORT_DATE_FORMAT));
            controller.setDateTo(dateTo.getValue().format(Constants.REPORT_DATE_FORMAT));
            controller.setReportType(reportAfterSelection);
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void generateReport(ReportType type) {
        String dateFrom = this.dateFrom.getValue().format(Constants.REPORT_DATE_FORMAT);
        String dateTo = this.dateTo.getValue().format(Constants.REPORT_DATE_FORMAT);
        switch (type) {
            case ITEMS_SOLD:
                ArrayList<CompactReportItem> itemsSold = ReportsRepository.getQuantitiesOfSoldItems(dateFrom, dateTo);
                // generate report
                break;
            case ITEMS_PURCHASED:
                ArrayList<CompactReportItem> itemsPurchased = ReportsRepository.getQuantitiesOfPurchasedItems(dateFrom, dateTo);
                // generate report
                break;
            case SALES:
                ArrayList<Sale> sales = ReportsRepository.getInfoOfSales(dateFrom, dateTo);
                // generate report
                break;
            case PURCHASES:
                ArrayList<Receiving> purchases = ReportsRepository.getInfoOfPurchases(dateFrom, dateTo);
                // generate report
                break;
            case INCOME:
                ArrayList<Income> incomes = ReportsRepository.getIncomes(dateFrom, dateTo);
                // generate report
                break;
            case EXPENSES:
                ArrayList<Expense> expenses = ReportsRepository.getExpenses(dateFrom, dateTo);
                // generate report
                break;
            case CUSTOMERS:
                ArrayList<Customer> customers = ReportsRepository.getCustomers(dateFrom, dateTo);
                ArrayList<Customer> regulars = ReportsRepository.getRegularCustomers(dateFrom, dateTo);
                // generate report
                break;
            case SUPPLIERS:
                ArrayList<Supplier> suppliers = ReportsRepository.getSuppliers(dateFrom, dateTo);
                // generate report
                break;
        }
    }
}
