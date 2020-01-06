package com.libanminds.main_controllers;

import com.jfoenix.controls.JFXButton;
import com.libanminds.dialogs_controllers.SelectCustomerDialogController;
import com.libanminds.dialogs_controllers.SelectSupplierDialogController;
import com.libanminds.models.*;
import com.libanminds.models.reports_models.CompactReportItem;
import com.libanminds.repositories.ReportsRepository;
import com.libanminds.utils.Constants;
import com.libanminds.utils.Views;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    @FXML
    private DatePicker dateFrom;

    @FXML
    private DatePicker dateTo;

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

    @FXML
    private VBox advancedOptions;

    // Customer advanced options
    private Customer selectedCustomer;
    private JFXButton customerSelectionBtn = new JFXButton();
    private Label customerName = new Label();
    private HBox customerNameWithBtn = new HBox();
    private JFXButton customerGenerateReportBtn = new JFXButton("Generate Report");
    private ToggleGroup customerSorting = new ToggleGroup();
    private final Label sortByLabel = new Label("Sort By:");
    private RadioButton customerSortByDate = new RadioButton("Date");
    private RadioButton customerSortBySales = new RadioButton("Sales");
    private HBox sortingRow = new HBox();

    // Supplier advanced options
    private Supplier selectedSupplier;
    private JFXButton supplierSelectionBtn = new JFXButton();
    private Label supplierName = new Label();
    private HBox supplierNameWithBtn = new HBox();
    private JFXButton supplierGenerateReportBtn = new JFXButton("Generate Report");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtons();
        initDates();
        initAdvancedOptions();
        initStyles();
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
        if (selectedCustomer == null) {
            customerName.setText("All Customers");
            customerSelectionBtn.setText("Select Customer");
            customerSelectionBtn.getStyleClass().remove(Constants.CSS_RED_BTN);
            customerSelectionBtn.getStyleClass().add(Constants.CSS_MAIN_BTN);
            sortingRow.setVisible(true);
        } else {
            customerName.setText(selectedCustomer.getName());
            customerSelectionBtn.setText("Clear");
            customerSelectionBtn.getStyleClass().remove(Constants.CSS_MAIN_BTN);
            customerSelectionBtn.getStyleClass().add(Constants.CSS_RED_BTN);
            sortingRow.setVisible(false);
        }
    }

    public void setSelectedSupplier(Supplier selectedSupplier) {
        this.selectedSupplier = selectedSupplier;
        if (selectedSupplier == null) {
            supplierName.setText("All Suppliers");
            supplierSelectionBtn.setText("Select Supplier");
            supplierSelectionBtn.getStyleClass().remove(Constants.CSS_RED_BTN);
            supplierSelectionBtn.getStyleClass().add(Constants.CSS_MAIN_BTN);
        } else {
            supplierName.setText(selectedSupplier.getName());
            supplierSelectionBtn.setText("Clear");
            supplierSelectionBtn.getStyleClass().remove(Constants.CSS_MAIN_BTN);
            supplierSelectionBtn.getStyleClass().add(Constants.CSS_RED_BTN);
        }
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
        customerSelectionBtn.setOnMouseClicked(event -> {
            if (selectedCustomer == null) showSelectCustomerDialog();
            else setSelectedCustomer(null);
        });
        customerGenerateReportBtn.setOnMouseClicked(event -> generateCustomersReport());
        supplierSelectionBtn.setOnMouseClicked(event -> {
            if (selectedSupplier == null) showSelectSupplierDialog();
            else setSelectedSupplier(null);
        });
        supplierGenerateReportBtn.setOnMouseClicked(event -> generateSuppliersReport());
    }

    private void initDates() {
        LocalDate today = LocalDate.now();
        dateFrom.setValue(today.minusMonths(1));
        dateTo.setValue(today);
    }

    private void initAdvancedOptions() {
        // Customers
        customerNameWithBtn.getChildren().addAll(customerName, customerSelectionBtn);
        setSelectedCustomer(null);
        customerSortByDate.setToggleGroup(customerSorting);
        customerSortBySales.setToggleGroup(customerSorting);
        customerSortBySales.fire();
        sortingRow.getChildren().addAll(sortByLabel, customerSortBySales, customerSortByDate);

        // Suppliers
        supplierNameWithBtn.getChildren().addAll(supplierName, supplierSelectionBtn);
        setSelectedSupplier(null);
    }

    private void initStyles() {
        Font h2 = Font.font(Font.getDefault().getName(), FontWeight.BOLD, 16);
        Font h3 = Font.font(Font.getDefault().getName(), FontWeight.SEMI_BOLD, 16);

        // Customers
        customerGenerateReportBtn.getStyleClass().addAll(Constants.CSS_GREEN_BTN, Constants.CSS_BTN_WITH_MIN_WIDTH);
        customerNameWithBtn.setAlignment(Pos.CENTER_LEFT);
        customerNameWithBtn.setSpacing(24);
        customerName.setFont(h2);
        sortByLabel.setFont(h2);
        sortByLabel.setPadding(new Insets(0, 12, 0, 0));
        sortingRow.setSpacing(24);
        sortingRow.setAlignment(Pos.CENTER_LEFT);
        customerSelectionBtn.setFont(h3);
        customerGenerateReportBtn.setFont(h3);
        customerSortBySales.setFont(h3);
        customerSortByDate.setFont(h3);

        // Suppliers
        supplierGenerateReportBtn.getStyleClass().addAll(Constants.CSS_GREEN_BTN, Constants.CSS_BTN_WITH_MIN_WIDTH);
        supplierNameWithBtn.setAlignment(Pos.CENTER_LEFT);
        supplierNameWithBtn.setSpacing(24);
        supplierName.setFont(h2);
        supplierSelectionBtn.setFont(h3);
        supplierGenerateReportBtn.setFont(h3);
    }

    private void showSelectCustomerDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.SELECT_CUSTOMER));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            SelectCustomerDialogController controller = loader.getController();
            controller.setFromReports(true);
            controller.setReportsController(this);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSelectSupplierDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.SELECT_SUPPLIER));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            SelectSupplierDialogController controller = loader.getController();
            controller.setFromReports(true);
            controller.setReportsController(this);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCustomersAdvancedOptions() {
        advancedOptions.getChildren().clear();
        advancedOptions.getChildren().addAll(customerNameWithBtn, sortingRow, customerGenerateReportBtn);
    }

    private void showSuppliersAdvancedOptions() {
        advancedOptions.getChildren().clear();
        advancedOptions.getChildren().addAll(supplierNameWithBtn, supplierGenerateReportBtn);
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
        advancedOptions.getChildren().clear();
        if (selectedCustomer != null) {
            ArrayList<CompactReportItem> reportItems = ReportsRepository.getItemsBoughtByCustomer(
                    selectedCustomer.getID(),
                    dateFrom.getValue().format(Constants.REPORT_DATE_FORMAT),
                    dateTo.getValue().format(Constants.REPORT_DATE_FORMAT)
            );
        } else if (customerSortByDate.isArmed()) {
            ArrayList<Customer> customers = ReportsRepository.getCustomers(
                    dateFrom.getValue().format(Constants.REPORT_DATE_FORMAT),
                    dateTo.getValue().format(Constants.REPORT_DATE_FORMAT)
            );
        } else {
            ArrayList<Customer> regulars = ReportsRepository.getRegularCustomers(
                    dateFrom.getValue().format(Constants.REPORT_DATE_FORMAT),
                    dateTo.getValue().format(Constants.REPORT_DATE_FORMAT)
            );
        }
    }

    private void generateSuppliersReport() {
        advancedOptions.getChildren().clear();
        if (selectedSupplier != null) {
            ArrayList<CompactReportItem> reportItems = ReportsRepository.getItemsBoughtFromSupplier(
                    selectedSupplier.getID(),
                    dateFrom.getValue().format(Constants.REPORT_DATE_FORMAT),
                    dateTo.getValue().format(Constants.REPORT_DATE_FORMAT)
            );
        }else  {
            ArrayList<Supplier> suppliers = ReportsRepository.getSuppliers(
                    dateFrom.getValue().format(Constants.REPORT_DATE_FORMAT),
                    dateTo.getValue().format(Constants.REPORT_DATE_FORMAT)
            );
        }
    }
}
