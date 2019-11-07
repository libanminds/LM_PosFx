package com.libanminds.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Item {
    private SimpleStringProperty imageUrl;
    private SimpleIntegerProperty code;
    private SimpleStringProperty name;
    private SimpleStringProperty category;
    private SimpleDoubleProperty price;
    private SimpleIntegerProperty stock;
    private SimpleStringProperty supplier;
    private SimpleStringProperty description;
    private SimpleDoubleProperty priceIncludingTax;
    private SimpleBooleanProperty isService;
    private SimpleStringProperty lastModified;

    public Item(String imageUrl, int code, String name, String category, double price, int stock, String supplier, String description, double priceIncludingTax, boolean isService, String lastModified) {
        this.imageUrl = new SimpleStringProperty(imageUrl);
        this.code = new SimpleIntegerProperty(code);
        this.name = new SimpleStringProperty(name);
        this.category = new SimpleStringProperty(category);
        this.price = new SimpleDoubleProperty(price);
        this.stock = new SimpleIntegerProperty(stock);
        this.supplier = new SimpleStringProperty(supplier);
        this.description = new SimpleStringProperty(description);
        this.priceIncludingTax = new SimpleDoubleProperty(priceIncludingTax);
        this.isService = new SimpleBooleanProperty(isService);
        this.lastModified = new SimpleStringProperty(lastModified);
    }

    public String getImageUrl() {
        return imageUrl.get();
    }

    public void setImageUrl(String val) {
        imageUrl.set(val);
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
        return category.get();
    }

    public void setCategory(String val) {
        category.set(val);
    }


    public double getPrice() {
        return price.get();
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

    public double getPriceIncludingTax() {
        return priceIncludingTax.get();
    }

    public void setPriceIncludingTax(double val) {
        priceIncludingTax.set(val);
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
