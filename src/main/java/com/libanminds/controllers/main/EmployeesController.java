package com.libanminds.controllers.main;

import com.libanminds.constants.AuthorizationKeys;
import com.libanminds.constants.Views;
import com.libanminds.controllers.dialogs.EmployeeDialogController;
import com.libanminds.models.User;
import com.libanminds.repositories.UsersRepository;
import com.libanminds.utils.Authorization;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeesController implements Initializable {
    @FXML private HBox buttonsHolder;
    @FXML private TextField searchField;
    @FXML private Button deleteEmployee;
    @FXML private Button editEmployee;
    @FXML private Button newEmployeeButton;
    @FXML private TableView<User> employeesTable;

    User selectedEmployee;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initSearch();
        initButtons();
        setTableListener();
        handleAuthorization();
    }

    private void handleAuthorization() {
        if (Authorization.cannot(AuthorizationKeys.ADD_USER)) buttonsHolder.getChildren().remove(newEmployeeButton);
        if (Authorization.cannot(AuthorizationKeys.EDIT_USER)) buttonsHolder.getChildren().remove(editEmployee);
        if (Authorization.cannot(AuthorizationKeys.DELETE_USER)) buttonsHolder.getChildren().remove(deleteEmployee);
    }

    private void initButtons() {
        newEmployeeButton.setOnMouseClicked((EventHandler<Event>) event -> showEmployeeDialog(null));
        editEmployee.setOnMouseClicked((EventHandler<Event>) event -> showEmployeeDialog(selectedEmployee));
        deleteEmployee.setOnMouseClicked((EventHandler<Event>) event -> showDeleteConfirmationDialog());
        deleteEmployee.setDisable(selectedEmployee == null);
        editEmployee.setDisable(selectedEmployee == null);
    }

    private void showEmployeeDialog(User employee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.ADD_EMPLOYEE));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            EmployeeDialogController controller = loader.getController();
            controller.initData(employee);
            stage.show();
            stage.setOnHidden(e -> employeesTable.setItems(UsersRepository.getUsers()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDeleteConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete " + selectedEmployee.getName() + "?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == yesButton) {
            boolean successful = UsersRepository.deleteUser(selectedEmployee);
            if (successful)
                employeesTable.getItems().remove(selectedEmployee);
        } else {
            alert.close();
        }
    }

    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            employeesTable.setItems(UsersRepository.getUsersLike(newValue));
        });
    }

    private void initializeTable() {
        TableColumn<User, String> usernameCol = new TableColumn<>("username");
        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        TableColumn<User, String> phoneCol = new TableColumn<>("Phone");
        TableColumn<User, Double> addressCol = new TableColumn<>("Address");

        employeesTable.getColumns().addAll(usernameCol, nameCol, roleCol, emailCol, phoneCol, addressCol);

        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("roleName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        employeesTable.setItems(UsersRepository.getUsers());
    }

    private void setTableListener() {
        employeesTable.selectionModelProperty().get().selectedItemProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
            selectedEmployee = (User) newValue;
            deleteEmployee.setDisable(selectedEmployee == null);
            editEmployee.setDisable(selectedEmployee == null);
        });
    }
}
