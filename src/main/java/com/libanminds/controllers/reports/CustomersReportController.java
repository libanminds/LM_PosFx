package com.libanminds.controllers.reports;

import com.jfoenix.controls.JFXButton;
import com.libanminds.constants.Constants;
import com.libanminds.constants.Views;
import com.libanminds.controllers.dialogs.SelectCustomerDialogController;
import com.libanminds.models.Customer;
import com.libanminds.models.reports_models.CompactReportItem;
import com.libanminds.repositories.ReportsRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomersReportController implements Initializable {
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;
    @FXML private JFXButton generateReportBtn;
    @FXML private JFXButton customerSelectionBtn;
    @FXML private Label customerName;
    @FXML private RadioButton sortByDate;
    @FXML private RadioButton sortBySales;
    @FXML private HBox sortingRow;

    private ToggleGroup customerSorting = new ToggleGroup();
    private Customer selectedCustomer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDates();
        initButtons();
        initRadioButtons();
        setSelectedCustomer(null);
    }

    private void initDates() {
        LocalDate today = LocalDate.now();
        dateFrom.setValue(today.minusMonths(1));
        dateTo.setValue(today);
    }

    private void initButtons() {
        customerSelectionBtn.setOnMouseClicked(event -> {
            if (selectedCustomer == null) showSelectCustomerDialog();
            else setSelectedCustomer(null);
        });
        generateReportBtn.setOnMouseClicked(event -> generateCustomersReport());
    }

    private void initRadioButtons() {
        sortByDate.setToggleGroup(customerSorting);
        sortBySales.setToggleGroup(customerSorting);
        sortBySales.fire();
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

    private void generateCustomersReport() {
        if (selectedCustomer != null) {
            ArrayList<CompactReportItem> reportItems = ReportsRepository.getItemsBoughtByCustomer(
                    selectedCustomer.getID(),
                    dateFrom.getValue().format(Constants.REPORT_DATE_FORMAT),
                    dateTo.getValue().format(Constants.REPORT_DATE_FORMAT)
            );
        } else if (sortByDate.isArmed()) {
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
}
