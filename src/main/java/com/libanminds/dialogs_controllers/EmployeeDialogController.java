package com.libanminds.dialogs_controllers;

import com.jfoenix.controls.JFXButton;
import com.libanminds.models.*;
import com.libanminds.repositories.CustomersRepository;
import com.libanminds.repositories.ExpensesRepository;
import com.libanminds.repositories.UsersRepository;
import com.libanminds.utils.Constants;
import com.libanminds.utils.GlobalSettings;
import com.libanminds.utils.HelperFunctions;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeDialogController implements Initializable {

    @FXML
    private TextField username;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField password;

    @FXML
    private TextField email;

    @FXML
    private TextField phone;

    @FXML
    private JFXButton cancel;

    @FXML
    private JFXButton save;

    @FXML
    private ChoiceBox<Role> role;

    @FXML
    private TextField employeeAddress;

    @FXML
    private Label errorMessagesLabel;


    int employeeID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initRoles();
        initSaveButton();
        initCancelButton();
        initErrorMessages();
    }

    public void initData(User employee) {
        if (employee != null) {
            employeeID = employee.getID() ;
            username.setText(employee.getUsername());
            firstName.setText(employee.getFirstName());
            lastName.setText(employee.getLastName());
            password.setText(employee.getPassword());
            email.setText(employee.getEmail());
            phone.setText(employee.getPhone());
            //role.setValue();
            employeeAddress.setText(employee.getAddress());
        } else
            employeeID = -1;
    }

    private void initErrorMessages() {
        errorMessagesLabel.setText("");
    }

    private void initRoles() {
        Role[] roles = Constants.ROLES;

        role.setItems(FXCollections.observableArrayList(roles));
        role.setValue(GlobalSettings.DEFAULT_USER_ROLE);
    }

    private boolean validateInput() {
        boolean isValid = true;

        if(username.getText().isEmpty()) {
            HelperFunctions.highlightTextfieldError(username);
            isValid = false;
        }else if(UsersRepository.usernameExists(username.getText())) {
            HelperFunctions.highlightTextfieldError(username);
            errorMessagesLabel.setText("Username already exists.");
            return false;
        }

        if(firstName.getText().isEmpty()) {
            HelperFunctions.highlightTextfieldError(firstName);
            isValid = false;
        }

        if(password.getText().isEmpty()) {
            HelperFunctions.highlightTextfieldError(password);
            isValid = false;
        }

        if(!isValid) errorMessagesLabel.setText("Please fill in all the required fields");
        return isValid;
    }

    private void initSaveButton() {
        save.setOnMouseClicked((EventHandler<Event>) event -> {
            if(!validateInput()) return;
            boolean successful;

            if(employeeID == -1)
                successful = UsersRepository.addUser(new User(
                        employeeID,
                        username.getText(),
                        firstName.getText(),
                        lastName.getText(),
                        password.getText(),
                        email.getText(),
                        phone.getText(),
                        role.getValue().getName(),
                        employeeAddress.getText()
                ));
            else
                successful = UsersRepository.updateUser(new User(
                        employeeID,
                        username.getText(),
                        firstName.getText(),
                        lastName.getText(),
                        password.getText(),
                        email.getText(),
                        phone.getText(),
                        role.getValue().getName(),
                        employeeAddress.getText()
                ));

            if(successful) {
                Stage currentStage = (Stage) save.getScene().getWindow();
                currentStage.close();
            }else {
                //TODO : DO SOMETHING
            }
        });
    }

    private void initCancelButton() {
        cancel.setOnMouseClicked((EventHandler<Event>) event -> {
            Stage currentStage = (Stage) save.getScene().getWindow();
            currentStage.close();
        });
    }
}
