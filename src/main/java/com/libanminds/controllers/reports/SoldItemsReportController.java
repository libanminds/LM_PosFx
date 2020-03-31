package com.libanminds.controllers.reports;

import com.jfoenix.controls.JFXButton;
import com.libanminds.constants.Constants;
import com.libanminds.controllers.main.MainController;
import com.libanminds.models.reports_models.CompactReportItem;
import com.libanminds.repositories.ReportsRepository;
import com.libanminds.utils.PDFGenerator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SoldItemsReportController implements Initializable {
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;
    @FXML private RadioButton sa1;
    @FXML private RadioButton sa2;
    @FXML private JFXButton generateReportBtn;

    private ToggleGroup categoryToggle = new ToggleGroup();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDates();
        initButtons();
        initRadioButtons();
    }

    private void initDates() {
        LocalDate today = LocalDate.now();
        dateFrom.setValue(today.minusMonths(1));
        dateTo.setValue(today);
    }

    private void initButtons() {
        generateReportBtn.setOnMouseClicked(event -> generateItemsSoldReport());
    }

    private void initRadioButtons() {
        sa1.setToggleGroup(categoryToggle);
        sa2.setToggleGroup(categoryToggle);
        sa1.fire();
    }

    private void generateItemsSoldReport() {
        ArrayList<CompactReportItem> itemsSold = ReportsRepository.getQuantitiesOfSoldItems(
                dateFrom.getValue().format(Constants.REPORT_DATE_FORMAT),
                dateTo.getValue().format(Constants.REPORT_DATE_FORMAT)
        );
        PDFGenerator.generateDemoPDF(MainController.instance.getStage());
    }

}
