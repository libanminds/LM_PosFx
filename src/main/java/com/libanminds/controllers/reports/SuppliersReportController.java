package com.libanminds.controllers.reports;

import com.jfoenix.controls.JFXButton;
import com.libanminds.constants.Constants;
import com.libanminds.constants.Views;
import com.libanminds.controllers.dialogs.SelectSupplierDialogController;
import com.libanminds.models.Supplier;
import com.libanminds.models.reports_models.CompactReportItem;
import com.libanminds.repositories.ReportsRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SuppliersReportController implements Initializable {
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;
    @FXML private JFXButton generateReportBtn;
    @FXML private JFXButton supplierSelectionBtn;
    @FXML private Label supplierName;

    private Supplier selectedSupplier;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDates();
        initButtons();
        setSelectedSupplier(null);
    }

    private void initDates() {
        LocalDate today = LocalDate.now();
        dateFrom.setValue(today.minusMonths(1));
        dateTo.setValue(today);
    }

    private void initButtons() {
        supplierSelectionBtn.setOnMouseClicked(event -> {
            if (selectedSupplier == null) showSelectSupplierDialog();
            else setSelectedSupplier(null);
        });
        generateReportBtn.setOnMouseClicked(event -> generateSuppliersReport());
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

    private void generateSuppliersReport() {
        if (selectedSupplier != null) {
            ArrayList<CompactReportItem> reportItems = ReportsRepository.getItemsBoughtFromSupplier(
                    selectedSupplier.getID(),
                    dateFrom.getValue().format(Constants.REPORT_DATE_FORMAT),
                    dateTo.getValue().format(Constants.REPORT_DATE_FORMAT)
            );
        } else {
            ArrayList<Supplier> suppliers = ReportsRepository.getSuppliers(
                    dateFrom.getValue().format(Constants.REPORT_DATE_FORMAT),
                    dateTo.getValue().format(Constants.REPORT_DATE_FORMAT)
            );
        }
    }
}
