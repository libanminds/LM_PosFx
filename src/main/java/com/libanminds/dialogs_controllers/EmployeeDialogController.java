package com.libanminds.dialogs_controllers;

import com.libanminds.models.Role;
import com.libanminds.models.User;
import com.libanminds.repositories.RolesRepository;
import com.libanminds.repositories.UsersRepository;
import com.libanminds.utils.HelperFunctions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeDialogController implements Initializable {

    int employeeID;
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
    private Button cancel;
    @FXML
    private Button save;
    @FXML
    private ChoiceBox<Role> role;
    @FXML
    private TextField employeeAddress;
    @FXML
    private Label errorMessagesLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initRoles();
        initSaveButton();
        initCancelButton();
        initErrorMessages();
    }

    public void initData(User employee) {
        if (employee != null) {
            employeeID = employee.getID();
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
        ObservableList<Role> roles = RolesRepository.getRoles();
        role.setItems(FXCollections.observableList(roles));
        role.setValue(roles.get(0));
    }

    private boolean validateInput() {
        boolean isValid = true;

        if (username.getText().isEmpty()) {
            HelperFunctions.highlightTextfieldError(username);
            isValid = false;
        } else if (UsersRepository.usernameExists(username.getText())) {
            HelperFunctions.highlightTextfieldError(username);
            errorMessagesLabel.setText("Username already exists.");
            return false;
        }

        if (firstName.getText().isEmpty()) {
            HelperFunctions.highlightTextfieldError(firstName);
            isValid = false;
        }

        if (password.getText().isEmpty()) {
            HelperFunctions.highlightTextfieldError(password);
            isValid = false;
        }

        if (!isValid) errorMessagesLabel.setText("Please fill in all the required fields");
        return isValid;
    }

    private void initSaveButton() {
        save.setOnMouseClicked((EventHandler<Event>) event -> {
            if (!validateInput()) return;
            boolean successful;

            if (employeeID == -1)
                successful = UsersRepository.addUser(new User(
                        employeeID,
                        username.getText(),
                        firstName.getText(),
                        lastName.getText(),
                        password.getText(),
                        email.getText(),
                        phone.getText(),
                        role.getValue(),
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
                        role.getValue(),
                        employeeAddress.getText()
                ));

            if (successful) {
                Stage currentStage = (Stage) save.getScene().getWindow();
                currentStage.close();
            } else {
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
