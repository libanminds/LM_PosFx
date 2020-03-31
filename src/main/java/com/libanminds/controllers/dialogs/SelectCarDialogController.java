package com.libanminds.controllers.dialogs;

import com.libanminds.constants.AuthorizationKeys;
import com.libanminds.constants.Views;
import com.libanminds.controllers.main.NewSaleController;
import com.libanminds.models.Car;
import com.libanminds.repositories.CarsRepository;
import com.libanminds.utils.Authorization;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SelectCarDialogController implements Initializable {
    @FXML private TextField searchField;
    @FXML private Button newCarBtn;
    @FXML private TableView<Car> carsTable;

    private Car selectedCar;
    private NewSaleController saleController;
    private int ownerId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtons();
        initSearch();
        initializeTable();
        handleAuthorization();
    }

    public void setSaleController(NewSaleController controller) {
        saleController = controller;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
        carsTable.setItems(CarsRepository.getCustomerCars(ownerId));
    }

    private void handleAuthorization() {
        // If you can add a customer, you can add a car (for now)
        // TODO make it separate
        newCarBtn.setVisible(Authorization.can(AuthorizationKeys.ADD_CUSTOMER));
    }

    private void initButtons() {
        newCarBtn.setOnMouseClicked((EventHandler<Event>) event -> showNewCarDialog());
    }

    private void showNewCarDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.ADD_CAR));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            CarDialogController controller = loader.getController();
            controller.setHostController(this);
            controller.setOwnerId(ownerId);
            stage.show();
            stage.setOnHidden(e -> {
                if (selectedCar != null) {
                    sendDataBackToHost();
                    Stage currentStage = (Stage) newCarBtn.getScene().getWindow();
                    currentStage.close();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSelectedCar(Car car) {
        selectedCar = car;
    }

    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            carsTable.setItems(CarsRepository.getCustomerCarsLike(ownerId, newValue));
        });
    }

    private void initializeTable() {
        TableColumn<Car, String> makeCol = new TableColumn<>("Make");
        TableColumn<Car, String> modelCol = new TableColumn<>("Model");
        TableColumn<Car, String> yearCol = new TableColumn<>("Year");
        TableColumn<Car, String> plateCol = new TableColumn<>("Plate");
        TableColumn<Car, String> vinCol = new TableColumn<>("Vin");
        TableColumn<Car, String> currentOdometerCol = new TableColumn<>("Current Odometer");
        TableColumn<Car, String> nextServiceOdometerCol = new TableColumn<>("Next Service Odometer");

        carsTable.getColumns().addAll(makeCol, modelCol, yearCol, plateCol, vinCol, currentOdometerCol, nextServiceOdometerCol);

        makeCol.setCellValueFactory(new PropertyValueFactory<>("make"));
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        plateCol.setCellValueFactory(new PropertyValueFactory<>("plate"));
        vinCol.setCellValueFactory(new PropertyValueFactory<>("vin"));
        currentOdometerCol.setCellValueFactory(new PropertyValueFactory<>("currentOdometer"));
        nextServiceOdometerCol.setCellValueFactory(new PropertyValueFactory<>("nextServiceOdometer"));

        carsTable.setRowFactory(tv -> {
            TableRow<Car> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() >= 2 && (!row.isEmpty())) {
                    Car car = row.getItem();
                    setSelectedCar(car);
                    sendDataBackToHost();
                    Stage currentStage = (Stage) searchField.getScene().getWindow();
                    currentStage.close();
                }
            });
            return row;
        });

        carsTable.setItems(CarsRepository.getCustomerCars(ownerId));
    }

    private void sendDataBackToHost() {
        saleController.setSelectedCar(selectedCar);
    }
}
