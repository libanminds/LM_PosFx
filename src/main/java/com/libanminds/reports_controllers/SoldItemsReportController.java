package com.libanminds.reports_controllers;

import com.jfoenix.controls.JFXButton;
import com.libanminds.main_controllers.MainController;
import com.libanminds.models.reports_models.CompactReportItem;
import com.libanminds.repositories.ReportsRepository;
import com.libanminds.utils.Constants;
import com.libanminds.utils.PDFGenerator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SoldItemsReportController implements Initializable {
    @FXML
    private DatePicker dateFrom;

    @FXML
    private DatePicker dateTo;

    @FXML
    private JFXButton generateReportBtn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDates();
        initButtons();
    }

    private void initDates() {
        LocalDate today = LocalDate.now();
        dateFrom.setValue(today.minusMonths(1));
        dateTo.setValue(today);
    }

    private void initButtons() {
        generateReportBtn.setOnMouseClicked(event -> generateItemsSoldReport());
    }

    private void generateItemsSoldReport() {
        ArrayList<CompactReportItem> itemsSold = ReportsRepository.getQuantitiesOfSoldItems(
                dateFrom.getValue().format(Constants.REPORT_DATE_FORMAT),
                dateTo.getValue().format(Constants.REPORT_DATE_FORMAT)
        );
        PDFGenerator.generateDemoPDF(MainController.instance.getStage());
    }

}
