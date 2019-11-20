package com.libanminds.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;

public class Item {
    private int id;
    private String  imageUrl;
    private String  currency;
    private int minStock;
    private ImageView image;
    private SimpleIntegerProperty code;
    private SimpleStringProperty name;
    private ItemCategory category;
    private SimpleDoubleProperty cost;
    private SimpleDoubleProperty price;
    private SimpleIntegerProperty stock;
    private SimpleStringProperty supplier;
    private SimpleStringProperty description;
    private SimpleBooleanProperty priceIncludesTax;
    private SimpleBooleanProperty isService;
    private SimpleStringProperty lastModified;

    public Item(int id, String imageUrl, int code, String name, ItemCategory category,double cost, double price, String currency, int stock, int minStock, String supplier, String description, boolean priceIncludesTax, boolean isService, String lastModified) {
        try{
            Image imageFile = new Image(new FileInputStream(imageUrl));
            image = new ImageView(imageFile);
            image.setFitHeight(50);
            image.setFitWidth(50);
            image.setPreserveRatio(true);
        }catch(Exception e) {
            image = null;
        }

        this.id = id;
        this.imageUrl = imageUrl;
        this.minStock = minStock;
        this.code = new SimpleIntegerProperty(code);
        this.name = new SimpleStringProperty(name);
        this.category = category;
        this.cost = new SimpleDoubleProperty(cost);
        this.price = new SimpleDoubleProperty(price);
        this.currency = currency;
        this.stock = new SimpleIntegerProperty(stock);
        this.supplier = new SimpleStringProperty(supplier);
        this.description = new SimpleStringProperty(description);
        this.priceIncludesTax = new SimpleBooleanProperty(priceIncludesTax);
        this.isService = new SimpleBooleanProperty(isService);
        this.lastModified = new SimpleStringProperty(lastModified);
    }

    public int getID() {
        return id;
    }

    public ItemCategory getItemCategory() {
        return category;
    }

    public void setImage(ImageView value) {
        image = value;
    }

    public ImageView getImage() {
        return image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getCode() {
        return code.get();
    }

    public void setCode(int val) {
        code.set(val);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String val) {
        name.set(val);
    }

    public String getCategory() {
        return category.getName();
    }

    public void setCategory(String val) {
        category.setName(val);
    }

    public double getCost() {
        return cost.get();
    }

    public void setCost(double val) {
        cost.set(val);
    }

    public double getPrice() {
        return price.get();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String val) {
        currency = val;
    }

    public void setPrice(double val) {
        price.set(val);
    }

    public int getStock() {
        return stock.get();
    }

    public void setStock(int val) {
        stock.set(val);
    }

    public int getMinStock() {
        return minStock;
    }

    public void setMinStock(int val) {
        minStock  = val;
    }

    public String getSupplier() {
        return supplier.get();
    }

    public void setSupplier(String val) {
        supplier.set(val);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String val) {
        description.set(val);
    }

    public boolean getPriceIncludesTax() {
        return priceIncludesTax.get();
    }

    public void setPriceIncludesTax(boolean val) {
        priceIncludesTax.set(val);
    }

    public boolean getIsService() {
        return isService.get();
    }

    public void setIsService(boolean val) {
        isService.set(val);
    }

    public String getLastModified() {
        return lastModified.get();
    }

    public void setLastModified(String val) {
        lastModified.set(val);
    }

}
