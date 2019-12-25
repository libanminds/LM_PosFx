package com.libanminds.main_controllers;

import com.libanminds.models.Customer;
import com.libanminds.models.GraphPoint;
import com.libanminds.repositories.DashboardRepository;
import com.libanminds.utils.Constants;
import com.libanminds.utils.GlobalSettings;
import com.libanminds.utils.Views;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    CategoryAxis salesChartX;
    @FXML
    NumberAxis salesChartY;
    @FXML
    private AreaChart<?, ?> salesChart;
    @FXML
    private PieChart pieChart;

    @FXML
    private Label salesOfToday;

    @FXML
    private Label expenseOfToday;

    @FXML
    private Label incomesOfToday;

    @FXML
    private Label revenueOfToday;

    @FXML
    private Label startNewSale;

    @FXML
    private Label addNewPurchase;

    @FXML
    private Label viewReports;

    @FXML
    private TableView<Customer> regularCustomersTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSalesChart();
        initQuickInsightsCards();
        initQuickLinksCards();
        initRegularCustomersTable();
        initPieChart();
    }

    private void initSalesChart() {
        salesChart.setTitle("Sales Over the Last " + GlobalSettings.DAYS_COUNT_OF_SALES_GRAPH + " Days");
        salesChartX.setLabel("Days");
        salesChartY.setLabel("Money in " + Constants.LIRA_CURRENCY);

        ArrayList<GraphPoint> dataPoints = DashboardRepository.getPointsOfGraph(Constants.LIRA_CURRENCY);

        XYChart.Series costs = new XYChart.Series();
        costs.setName("Item's Cost");

        for (GraphPoint dataPoint : dataPoints) {
            costs.getData().add(new XYChart.Data(dataPoint.getCategoryValue(), dataPoint.getNumberValue()));
        }

        salesChart.getData().addAll(costs);
    }

    private void initQuickInsightsCards() {
        //IMPORTANT THE ORDERING OF THESE STATEMENTS SHOULD NOT BE CHANGED.
        salesOfToday.setText(DashboardRepository.getSalesOfToday(Constants.LIRA_CURRENCY));
        expenseOfToday.setText(DashboardRepository.getExpensesOfToday(Constants.LIRA_CURRENCY));
        incomesOfToday.setText(DashboardRepository.getIncomesOfToday(Constants.LIRA_CURRENCY));
        revenueOfToday.setText(DashboardRepository.getRevenueOfToday(Constants.LIRA_CURRENCY));
    }

    private void initQuickLinksCards() {
        startNewSale.setOnMouseClicked((EventHandler<Event>) event -> LaunchNewSale());
        addNewPurchase.setOnMouseClicked((EventHandler<Event>) event -> LaunchNewReceiving());
        viewReports.setOnMouseClicked((EventHandler<Event>) event -> LaunchReports());
    }

    private void LaunchNewSale() {
        MainController.instance.onMenuClick(Views.NEW_SALE);
    }

    private void LaunchNewReceiving() {
        MainController.instance.onMenuClick(Views.NEW_RECEIVING);
    }

    private void LaunchReports() {
        MainController.instance.onMenuClick(Views.REPORTS);
    }

    private void initRegularCustomersTable() {

        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        TableColumn<Customer, String> phoneCol = new TableColumn<>("Phone");

        regularCustomersTable.getColumns().addAll(nameCol, phoneCol);

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        regularCustomersTable.setItems(DashboardRepository.getRegularCustomers());
    }

    private void initPieChart() {

        ArrayList<GraphPoint> dataPoints = DashboardRepository.getMostSoldItems();

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();

        for (GraphPoint dataPoint : dataPoints)
            pieChartData.add(new PieChart.Data(dataPoint.getCategoryValue(), dataPoint.getNumberValue()));

        pieChart.getData().addAll(pieChartData);
    }
}
