package com.libanminds.dialogs_controllers;

import com.libanminds.models.Car;
import com.libanminds.repositories.CarsRepository;
import com.libanminds.utils.HelperFunctions;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CarDialogController implements Initializable {

    @FXML
    private TextField make;
    @FXML
    private TextField model;
    @FXML
    private TextField year;
    @FXML
    private TextField currentOdometer;
    @FXML
    private TextField nextServiceOdometer;
    @FXML
    private TextField vin;
    @FXML
    private TextField plate;
    @FXML
    private Button cancel;
    @FXML
    private Button save;

    @FXML
    private Label errorMessagesLabel;

    private int carID = -1;
    private int ownerId = -1;
    private SelectCarDialogController hostController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSaveButton();
        initCancelButton();
        initErrorMessages();
    }

    public void setHostController(SelectCarDialogController controller) {
        hostController = controller;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void initData(Car car) {
        if (car != null) {
            carID = car.getID();
            ownerId = car.getOwnerID();
            make.setText(car.getMake());
            model.setText(car.getModel());
            year.setText(car.getYear());
            currentOdometer.setText(car.getCurrentOdometer());
            nextServiceOdometer.setText(car.getNextServiceOdometer());
            plate.setText(car.getPlate());
            vin.setText(car.getVin());
        }
    }

    private void initErrorMessages() {
        errorMessagesLabel.setText("");
    }

    private void initSaveButton() {
        save.setOnMouseClicked((EventHandler<Event>) event -> {

            if (!validateInput())
                return;

            boolean successful;
            Car newCar = new Car(
                    carID,
                    ownerId,
                    make.getText(),
                    model.getText(),
                    year.getText(),
                    currentOdometer.getText(),
                    nextServiceOdometer.getText(),
                    vin.getText(),
                    plate.getText()
            );

            if (carID == -1)
                successful = CarsRepository.addCar(newCar);
            else
                successful = CarsRepository.updateCar(newCar);

            if (successful) {
                if (hostController != null) {
                    hostController.setSelectedCar(newCar);
                }
                Stage currentStage = (Stage) save.getScene().getWindow();
                currentStage.close();
            } else {
                //TODO : DO SOMETHING
            }
        });
    }

    private boolean validateInput() {
        if (plate.getText().isEmpty()) {
            errorMessagesLabel.setText("Please fill in all the required fields");
            HelperFunctions.highlightTextfieldError(plate);
            return false;
        }

        return true;
    }

    private void initCancelButton() {
        cancel.setOnMouseClicked((EventHandler<Event>) event -> {
            Stage currentStage = (Stage) save.getScene().getWindow();
            currentStage.close();
        });
    }
}
