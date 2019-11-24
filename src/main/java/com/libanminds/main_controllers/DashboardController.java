package com.libanminds.main_controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private AreaChart<?,?> salesChart;

    @FXML
    NumberAxis salesChartX;

    @FXML
    NumberAxis salesChartY;

    @FXML
    private PieChart pieChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSalesChart();
        initPieChart();
    }

    private void initSalesChart() {
        salesChart.setTitle("Sales Over the Last X Days");
        salesChartX.setLabel("Days");
        salesChartY.setLabel("Money in Dollars");

        XYChart.Series costs= new XYChart.Series();
        costs.setName("Item's Cost");
        costs.getData().add(new XYChart.Data(0, 4));
        costs.getData().add(new XYChart.Data(2, 10));
        costs.getData().add(new XYChart.Data(3, 15));
        costs.getData().add(new XYChart.Data(4, 8));
        costs.getData().add(new XYChart.Data(5, 5));
        costs.getData().add(new XYChart.Data(6, 18));
        costs.getData().add(new XYChart.Data(7, 15));
        costs.getData().add(new XYChart.Data(8, 13));
        costs.getData().add(new XYChart.Data(9, 19));
        costs.getData().add(new XYChart.Data(10, 21));
        costs.getData().add(new XYChart.Data(11, 21));

        XYChart.Series prices = new XYChart.Series();
        prices.setName("Item's Prices");
        prices.getData().add(new XYChart.Data(1, 20));
        prices.getData().add(new XYChart.Data(2, 15));
        prices.getData().add(new XYChart.Data(3, 13));
        prices.getData().add(new XYChart.Data(4, 12));
        prices.getData().add(new XYChart.Data(5, 14));
        prices.getData().add(new XYChart.Data(6, 18));
        prices.getData().add(new XYChart.Data(7, 25));
        prices.getData().add(new XYChart.Data(8, 25));
        prices.getData().add(new XYChart.Data(9, 23));
        prices.getData().add(new XYChart.Data(10, 26));
        prices.getData().add(new XYChart.Data(11, 26));

        salesChart.getData().addAll(costs);
    }

    private void initPieChart() {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Grapefruit", 13),
                        new PieChart.Data("Oranges", 25),
                        new PieChart.Data("Plums", 10),
                        new PieChart.Data("Pears", 22),
                        new PieChart.Data("Apples", 30));


        pieChart.getData().addAll(pieChartData);
    }
}
