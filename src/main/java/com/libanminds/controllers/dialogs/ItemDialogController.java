package com.libanminds.controllers.dialogs;

import com.libanminds.constants.Constants;
import com.libanminds.models.Item;
import com.libanminds.models.ItemCategory;
import com.libanminds.repositories.ItemsCategoriesRepository;
import com.libanminds.repositories.ItemsRepository;
import com.libanminds.singletons.GlobalSettings;
import com.libanminds.utils.HelperFunctions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ItemDialogController implements Initializable {
    @FXML private TextField code;
    @FXML private TextField name;
    @FXML private TextField cost;
    @FXML private TextField price;
    @FXML private Button cancel;
    @FXML private Button save;
    @FXML private ChoiceBox<String> currency;
    @FXML private TextField quantity;
    @FXML private TextField description;
    @FXML private TextField limit;
    @FXML private CheckBox includesTax;
    @FXML private Label errorMessagesLabel;
    @FXML private ChoiceBox<ItemCategory> category;
    @FXML private Button chooseImage;
    @FXML private ImageView imageDisplay;

    private File itemImage;

    private int itemID = -1;
    private int itemPropertiesID = -1;

    private boolean itemImageChanged;
    private String imagePath;

    private SelectItemDialogController hostController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSaveButton();
        initCancelButton();
        initChoiceBoxes();
        initImagePicker();
        initFilters();
        initErrorMessages();
    }

    public void setHostController(SelectItemDialogController controller) {
        hostController = controller;
    }

    public void initData(Item item) {
        if (item != null) {
            itemID = item.getID();

            try {
                imageDisplay.setImage(new Image(new File(item.getImageUrl()).toURI().toString()));
            } catch (Exception e) {
                System.out.println("Image not working: " + e.getMessage());
            }
            code.setText(item.getCode() + "");
            name.setText(item.getName());
            category.setValue(item.getItemCategory());
            cost.setText(item.getCost() + "");
            price.setText(item.getInitialPrice() + "");
            currency.setValue(item.getCurrency());
            quantity.setText(item.getStock() + "");
            limit.setText(item.getMinStock() + "");
            description.setText(item.getDescription());
            includesTax.setSelected(item.getPriceIncludesTax());
            itemImageChanged = false;
            imagePath = item.getImageUrl();
            itemPropertiesID = item.getItemPropertiesID();
        }
    }

    private void initErrorMessages() {
        errorMessagesLabel.setText("");
    }

    private void initFilters() {
        code.setTextFormatter(HelperFunctions.getUnsignedIntegerFilter());
        quantity.setTextFormatter(HelperFunctions.getUnsignedIntegerFilter());
        cost.setTextFormatter(HelperFunctions.getUnsignedNumberFilter());
        limit.setTextFormatter(HelperFunctions.getUnsignedIntegerFilter());
        price.setTextFormatter(HelperFunctions.getUnsignedNumberFilter());
    }

    private void initChoiceBoxes() {
        String[] currencies = {Constants.USD_CURRENCY, Constants.LBP_CURRENCY};
        currency.setItems(FXCollections.observableArrayList(currencies));
        currency.setValue(GlobalSettings.fetch().defaultCurrency);

        ObservableList<ItemCategory> categories = ItemsCategoriesRepository.getItemsCategories();
        category.setItems(FXCollections.observableList(categories));
        category.setValue(categories.get(0));
    }

    private boolean validateInput() {
        boolean isValid = true;
        if (code.getText().isEmpty()) {
            HelperFunctions.highlightTextfieldError(code);
            isValid = false;
        }

        if (name.getText().isEmpty()) {
            HelperFunctions.highlightTextfieldError(name);
            isValid = false;
        }

        if (limit.getText().isEmpty()) {
            HelperFunctions.highlightTextfieldError(limit);
            isValid = false;
        }

        if (cost.getText().isEmpty()) {
            HelperFunctions.highlightTextfieldError(cost);
            isValid = false;
        }

        if (quantity.getText().isEmpty()) {
            HelperFunctions.highlightTextfieldError(quantity);
            isValid = false;
        }

        if (price.getText().isEmpty()) {
            HelperFunctions.highlightTextfieldError(price);
            isValid = false;
        }

        if (!isValid) errorMessagesLabel.setText("Please fill in all the required fields");
        return isValid;
    }

    private void initSaveButton() {
        save.setOnMouseClicked((EventHandler<Event>) event -> {
            if (!validateInput()) return;
            boolean successful;

            Item newItem = new Item(
                    itemID,
                    itemID == -1 ? saveImageOnDevice(itemImage) : imagePath,
                    Integer.parseInt(code.getText()),
                    itemPropertiesID,
                    name.getText(),
                    category.getValue(),
                    Double.parseDouble(cost.getText()),
                    Double.parseDouble(price.getText()),
                    currency.getValue(),
                    Integer.parseInt(quantity.getText()),
                    Integer.parseInt(limit.getText()),
                    "Supplier",
                    description.getText(),
                    includesTax.isSelected(),
                    false,
                    ""
            );

            if (itemID == -1)
                successful = ItemsRepository.addItem(newItem);
            else {
                if (itemImageChanged) {
                    //deleteFile(imagePath);
                    imagePath = saveImageOnDevice(itemImage);
                }

                successful = ItemsRepository.updateItem(newItem);
            }

            if (successful) {
                if (hostController != null) {
                    hostController.setSelectedItem(newItem);
                }
                Stage currentStage = (Stage) save.getScene().getWindow();
                currentStage.close();
            } else {
                //TODO : DO SOMETHING
            }
        });
    }

    //Be super careful when using this function (Though it's not working yet, maybe because the image is being used by the program)
    private boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists())
            return file.delete();

        return false;
    }

    private void initCancelButton() {
        cancel.setOnMouseClicked((EventHandler<Event>) event -> {
            Stage currentStage = (Stage) save.getScene().getWindow();
            currentStage.close();
        });
    }

    private void initImagePicker() {
        chooseImage.setOnMouseClicked((EventHandler<Event>) event -> {
            Stage currentStage = (Stage) save.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
            fileChooser.getExtensionFilters().add(extFilter);
            itemImage = fileChooser.showOpenDialog(currentStage);
            itemImageChanged = itemImage != null;
            try {
                imageDisplay.setImage(new Image(itemImage.toURI().toString()));
            } catch (Exception e) {

            }
        });
    }

    private String saveImageOnDevice(File imageFile) {
        String imagePath = "";
        try {
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            String fileExtension = HelperFunctions.getFileExtension(imageFile);
            File outputFile = new File(getImagePath(imageFile));
            ImageIO.write(bufferedImage, fileExtension, outputFile);
            imagePath = outputFile.getAbsolutePath();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return imagePath;
    }

    private String getImagePath(File file) {
        return Constants.ITEMS_IMAGES_FOLDER_PATH + System.currentTimeMillis() + file.getName();
    }
}