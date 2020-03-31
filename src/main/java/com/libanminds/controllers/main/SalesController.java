package com.libanminds.controllers.main;

import com.libanminds.constants.AuthorizationKeys;
import com.libanminds.constants.Views;
import com.libanminds.controllers.dialogs.CompleteSaleController;
import com.libanminds.controllers.dialogs.ReturnSaleItemsController;
import com.libanminds.controllers.dialogs.ViewSaleController;
import com.libanminds.models.Sale;
import com.libanminds.repositories.SalesRepository;
import com.libanminds.utils.Authorization;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SalesController implements Initializable {
    @FXML private HBox buttonsHolder;
    @FXML private TextField searchField;
    @FXML private Button completePayment;
    @FXML private Button viewSaleBtn;
    @FXML private Button returnItems;
    @FXML private TableView<Sale> salesTable;

    private Sale selectedSale;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtons();
        initSearch();
        initializeTable();
        setTableListener();
        handleAuthorization();
    }

    private void handleAuthorization() {
        if (Authorization.cannot(AuthorizationKeys.VIEW_SALE)) buttonsHolder.getChildren().remove(viewSaleBtn);
        if (Authorization.cannot(AuthorizationKeys.RETURN_SALE_ITEMS)) buttonsHolder.getChildren().remove(returnItems);
        if (Authorization.cannot(AuthorizationKeys.COMPLETE_SALE_PAYMENT))
            buttonsHolder.getChildren().remove(completePayment);
    }

    private void initButtons() {
        completePayment.setOnMouseClicked((EventHandler<Event>) event -> showCompleteSaleDialog(selectedSale));
        viewSaleBtn.setOnMouseClicked((EventHandler<Event>) event -> showViewSaleDialog(selectedSale));
        returnItems.setOnMouseClicked((EventHandler<Event>) event -> showReturnItemsDialog(selectedSale));

        viewSaleBtn.setDisable(true);
        completePayment.setDisable(true);
        returnItems.setDisable(true);
    }

    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> salesTable.setItems(SalesRepository.getSalesLike(newValue)));
    }

    private void showViewSaleDialog(Sale sale) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.VIEW_SALE));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            ViewSaleController controller = loader.getController();
            controller.setSale(sale);
            stage.show();
            stage.setOnHidden(e -> salesTable.setItems(SalesRepository.getSales()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showReturnItemsDialog(Sale sale) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.RETURN_SALE_ITEMS));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            ReturnSaleItemsController controller = loader.getController();
            controller.setSale(sale);
            stage.show();
            stage.setOnHidden(e -> salesTable.setItems(SalesRepository.getSales()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCompleteSaleDialog(Sale sale) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.COMPLETE_SALE));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            CompleteSaleController controller = loader.getController();
            controller.setSale(sale);
            stage.show();
            stage.setOnHidden(e -> salesTable.setItems(SalesRepository.getSales()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeTable() {
        TableColumn<Sale, String> customerName = new TableColumn<>("Customer Name");
        TableColumn<Sale, String> totalAmount = new TableColumn<>("Total Amount");
        TableColumn<Sale, String> paidAmount = new TableColumn<>("Paid Amount");
        TableColumn<Sale, String> discount = new TableColumn<>("Discount");
        TableColumn<Sale, String> remainingAmount = new TableColumn<>("Remaining Amount");
        TableColumn<Sale, String> paymentType = new TableColumn<>("Payment Type");

        salesTable.getColumns().addAll(customerName, totalAmount, paidAmount, discount, remainingAmount, paymentType);

        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        totalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmountFormatted"));
        paidAmount.setCellValueFactory(new PropertyValueFactory<>("paidAmountFormatted"));
        discount.setCellValueFactory(new PropertyValueFactory<>("discountFormatted"));
        remainingAmount.setCellValueFactory(new PropertyValueFactory<>("remainingAmountFormatted"));
        paymentType.setCellValueFactory(new PropertyValueFactory<>("paymentType"));

        salesTable.setItems(SalesRepository.getSales());
    }

    private void setTableListener() {
        salesTable.selectionModelProperty().get().selectedItemProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
            selectedSale = (Sale) newValue;
            completePayment.setDisable(selectedSale == null || selectedSale.isComplete());
            viewSaleBtn.setDisable(selectedSale == null);
            returnItems.setDisable(selectedSale == null);
        });
    }
}
