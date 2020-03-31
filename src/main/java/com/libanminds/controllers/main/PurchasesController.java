package com.libanminds.controllers.main;

import com.libanminds.constants.AuthorizationKeys;
import com.libanminds.constants.Views;
import com.libanminds.controllers.dialogs.CompletePurchaseController;
import com.libanminds.controllers.dialogs.ReturnPurchaseItemsController;
import com.libanminds.controllers.dialogs.ViewPurchaseController;
import com.libanminds.models.Purchase;
import com.libanminds.repositories.PurchasesRepository;
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

public class PurchasesController implements Initializable {
    @FXML private HBox buttonsHolder;
    @FXML private TextField searchField;
    @FXML private Button completePayment;
    @FXML private Button viewPurchase;
    @FXML private Button returnItems;
    @FXML private TableView<Purchase> purchasesTable;

    private Purchase selectedPurchase;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtons();
        initSearch();
        initializeTable();
        setTableListener();
        handleAuthorization();
    }

    private void handleAuthorization() {
        if (Authorization.cannot(AuthorizationKeys.VIEW_PURCHASE))
            buttonsHolder.getChildren().remove(viewPurchase);
        if (Authorization.cannot(AuthorizationKeys.RETURN_PURCHASE_ITEMS))
            buttonsHolder.getChildren().remove(returnItems);
        if (Authorization.cannot(AuthorizationKeys.COMPLETE_PURCHASE_PAYMENT))
            buttonsHolder.getChildren().remove(completePayment);
    }

    private void initButtons() {
        completePayment.setOnMouseClicked((EventHandler<Event>) event -> showCompletePurchaseDialog(selectedPurchase));
        viewPurchase.setOnMouseClicked((EventHandler<Event>) event -> showViewPurchaseDialog(selectedPurchase));
        returnItems.setOnMouseClicked((EventHandler<Event>) event -> showReturnItemsDialog(selectedPurchase));
        viewPurchase.setDisable(true);
        completePayment.setDisable(true);
        returnItems.setDisable(true);
    }

    private void showViewPurchaseDialog(Purchase purchase) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.VIEW_PURCHASE));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            ViewPurchaseController controller = loader.getController();
            controller.setPurchase(purchase);
            stage.show();
            stage.setOnHidden(e -> purchasesTable.setItems(PurchasesRepository.getPurchases()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showReturnItemsDialog(Purchase purchase) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.RETURN_PURCHASE_ITEMS));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            ReturnPurchaseItemsController controller = loader.getController();
            controller.setPurchase(purchase);
            stage.show();
            stage.setOnHidden(e -> purchasesTable.setItems(PurchasesRepository.getPurchases()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCompletePurchaseDialog(Purchase purchase) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.COMPLETE_PURCHASE));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            CompletePurchaseController controller = loader.getController();
            controller.setPurchase(purchase);
            stage.show();
            stage.setOnHidden(e -> purchasesTable.setItems(PurchasesRepository.getPurchases()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> purchasesTable.setItems(PurchasesRepository.getPurchasesLike(newValue)));
    }

    private void initializeTable() {
        TableColumn<Purchase, String> customerName = new TableColumn<>("Supplier Name");
        TableColumn<Purchase, String> totalAmount = new TableColumn<>("Total Amount");
        TableColumn<Purchase, String> paidAmount = new TableColumn<>("Paid Amount");
        TableColumn<Purchase, String> discount = new TableColumn<>("Discount");
        TableColumn<Purchase, String> remainingAmount = new TableColumn<>("Remaining Amount");
        TableColumn<Purchase, String> paymentType = new TableColumn<>("Payment Type");

        purchasesTable.getColumns().addAll(customerName, totalAmount, paidAmount, discount, remainingAmount, paymentType);

        customerName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        totalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmountFormatted"));
        paidAmount.setCellValueFactory(new PropertyValueFactory<>("paidAmountFormatted"));
        discount.setCellValueFactory(new PropertyValueFactory<>("discountFormatted"));
        remainingAmount.setCellValueFactory(new PropertyValueFactory<>("remainingAmountFormatted"));
        paymentType.setCellValueFactory(new PropertyValueFactory<>("paymentType"));

        purchasesTable.setItems(PurchasesRepository.getPurchases());
    }

    private void setTableListener() {
        purchasesTable.selectionModelProperty().get().selectedItemProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
            selectedPurchase = (Purchase) newValue;
            completePayment.setDisable(selectedPurchase == null || selectedPurchase.isComplete());
            viewPurchase.setDisable(selectedPurchase == null);
            returnItems.setDisable(selectedPurchase == null);
        });
    }
}
