package com.libanminds.dialogs_controllers;

import com.jfoenix.controls.JFXButton;
import com.libanminds.models.Expense;
import com.libanminds.models.Item;
import com.libanminds.models.Type;
import com.libanminds.repositories.ExpensesRepository;
import com.libanminds.repositories.ItemsRepository;
import com.libanminds.utils.Constants;
import com.libanminds.utils.HelperFunctions;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.Random;
import java.util.ResourceBundle;

public class ItemDialogController implements Initializable {

    @FXML
    private TextField code;

    @FXML
    private TextField name;

    @FXML
    private TextField cost;

    @FXML
    private TextField price;

    @FXML
    private JFXButton cancel;

    @FXML
    private JFXButton save;

    @FXML
    private ChoiceBox<String> currency;

    @FXML
    private TextField quantity;

    @FXML
    private TextField description;

    @FXML
    private TextField limit;

    @FXML
    private CheckBox includesTax;

    @FXML
    private ChoiceBox<?> category;

    @FXML
    private Button chooseImage;

    @FXML
    private ImageView imageDisplay;

    private File itemImage;

    private int itemID;

    private boolean itemImageChanged;
    private String imagePath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSaveButton();
        initCancelButton();
        initChoiceBoxes();
        initImagePicker();
    }

    public void initData(Item item) {
        if (item != null) {
            itemID = item.getID();

            try {
                imageDisplay.setImage(new Image(new File(item.getImageUrl()).toURI().toString()));
            }catch (Exception e) {
                System.out.println("Image not working: " + e.getMessage());
            }

            code.setText(item.getCode() + "");
            name.setText(item.getName());
            //category.setText(item.getName());
            cost.setText(item.getCost() + "");
            price.setText(item.getPrice() + "");
            currency.setValue(item.getCurrency());
            quantity.setText(item.getStock() + "");
            //name.setText(item.getName());
            description.setText(item.getDescription());
            includesTax.setSelected(item.getPriceIncludesTax());
            itemImageChanged = false;
            imagePath = item.getImageUrl();
        } else
            itemID = -1;
    }

    private void initChoiceBoxes() {
        String[] currencies = { "$", "LL" };
        currency.setItems(FXCollections.observableArrayList(currencies));
    }

    private void initSaveButton() {
        save.setOnMouseClicked((EventHandler<Event>) event -> {

            boolean successful;

            if(itemID == -1)
                successful = ItemsRepository.addItem(new Item(
                        itemID,
                        saveImageOnDevice(itemImage),
                        Integer.parseInt(code.getText()),
                        name.getText(),
                        "Category",
                        Double.parseDouble(cost.getText()),
                        Double.parseDouble(price.getText()),
                        currency.getValue(),
                        Integer.parseInt(quantity.getText()),
                        "Supplier",
                        description.getText(),
                        includesTax.isSelected(),
                        false,
                        ""
                ));
            else {
                if(itemImageChanged) {
                    deleteFile(imagePath);
                    imagePath = saveImageOnDevice(itemImage);
                }

                successful = ItemsRepository.updateItem(new Item(
                        itemID,
                        imagePath,
                        Integer.parseInt(code.getText()),
                        name.getText(),
                        "Category",
                        Double.parseDouble(cost.getText()),
                        Double.parseDouble(price.getText()),
                        currency.getValue(),
                        Integer.parseInt(quantity.getText()),
                        "Supplier",
                        description.getText(),
                        includesTax.isSelected(),
                        false,
                        ""
                ));
            }

            if(successful) {
                Stage currentStage = (Stage) save.getScene().getWindow();
                currentStage.close();
            }else {
                //TODO : DO SOMETHING
            }
        });
    }

    private boolean deleteFile(String filePath) {
        File file = new File(filePath);
            if(file.exists())
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
        chooseImage.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                Stage currentStage = (Stage) save.getScene().getWindow();
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
                fileChooser.getExtensionFilters().add(extFilter);
                itemImage = fileChooser.showOpenDialog(currentStage);
                itemImageChanged = itemImage != null;
                try {
                    imageDisplay.setImage(new Image(itemImage.toURI().toString()));
                }catch (Exception e) {

                }
            }
        });
    }

    private String saveImageOnDevice(File imageFile) {
        String imagePath = "";
        try {
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            String fileExtension= HelperFunctions.getFileExtension(imageFile);
            System.out.println("File extension : " + fileExtension);
            File outputFile = new File(getImagePath(imageFile));
            ImageIO.write(bufferedImage, fileExtension, outputFile);
            imagePath = outputFile.getAbsolutePath();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return imagePath;
    }

    private String getImagePath(File file) {
        return Constants.ITEMS_IMAGES_FOLDER_PATH + System.currentTimeMillis() + file.getName();//"." + HelperFunctions.getFileExtension(file);
    }
}