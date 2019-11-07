package com.libanminds.main_controllers;

import com.libanminds.models.Customer;
import com.libanminds.models.User;
import com.libanminds.repositories.CustomersRepository;
import com.libanminds.repositories.UsersRepository;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeesController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private Button newEmployeeButton;

    @FXML
    private TableView<User> employeesTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initSearch();
    }

    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            employeesTable.setItems(UsersRepository.getUsersLike(newValue));
        });
    }

    private void initializeTable() {
        TableColumn<User,String> usernameCol = new TableColumn<>("username");
        TableColumn<User,String> nameCol = new TableColumn<>("Name");
        TableColumn<User,String> roleCol = new TableColumn<>("Role");
        TableColumn<User,String> emailCol = new TableColumn<>("Email");
        TableColumn<User,String> phoneCol = new TableColumn<>("Phone");
        TableColumn<User,Double> addressCol = new TableColumn<>("Address");

        employeesTable.getColumns().addAll(usernameCol,nameCol,roleCol,emailCol,phoneCol,addressCol);

        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        employeesTable.setItems(UsersRepository.getUsers());
    }
}
