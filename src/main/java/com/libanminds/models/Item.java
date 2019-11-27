package com.libanminds.models;

import com.libanminds.utils.GlobalSettings;
import com.libanminds.utils.HelperFunctions;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;

public class Item {
    private SimpleDoubleProperty initialPrice;

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

    private int id;
    private String  imageUrl;
    private String  currency;
    private int minStock;
    private ImageView image;

    //These are used in the sales tab.
    private IntegerProperty saleQuantity = new SimpleIntegerProperty(1);
    private SimpleStringProperty saleCurrency = new SimpleStringProperty("LL");
    private DoubleProperty discount = new SimpleDoubleProperty(0);
    private DoubleProperty total = new SimpleDoubleProperty(0);
    private StringProperty totalWithCurrency = new SimpleStringProperty("0 LL");
    private StringProperty priceWithCurrency;

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
        this.initialPrice =  new SimpleDoubleProperty(price);
        this.currency = currency;
        this.stock = new SimpleIntegerProperty(stock);
        this.supplier = new SimpleStringProperty(supplier);
        this.description = new SimpleStringProperty(description);
        this.priceIncludesTax = new SimpleBooleanProperty(priceIncludesTax);
        this.isService = new SimpleBooleanProperty(isService);
        this.lastModified = new SimpleStringProperty(lastModified);
        this.price = new SimpleDoubleProperty(0);
        calculatePrice();
        priceWithCurrency = new SimpleStringProperty(price + currency);

        this.total.bind(this.price.multiply(this.saleQuantity).subtract(this.discount));
        //this.priceWithCurrency.bind(Bindings.concat(this.price, " ",this.saleCurrency));
        //this.totalWithCurrency.bind(Bindings.concat(this.total, " ",this.saleCurrency));
        this.priceWithCurrency.bind(Bindings.createStringBinding( () -> HelperFunctions.getDecimalFormatter().format(this.price.getValue()) + " " + this.saleCurrency.getValue(),this.price,this.saleCurrency));
        this.totalWithCurrency.bind(Bindings.createStringBinding( () -> HelperFunctions.getDecimalFormatter().format(this.total.getValue()) + " " + this.saleCurrency.getValue(),this.total,this.saleCurrency));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true ;

        if (! (other instanceof Item)) return false ;

        Item otherItem = (Item) other ;
        return this.id == otherItem.id;
    }

    public String getSaleCurrency() {
        return saleCurrency.getValue();
    }

    public void setSaleCurrency(String val) {
        saleCurrency.setValue(val);
        calculatePrice();
    }

    private void calculatePrice() {
        if(currency.equals(saleCurrency.getValue())) {
            price.setValue(initialPrice.getValue());
        } else {
            if(currency.equals("$"))
                price.setValue(initialPrice.getValue() * GlobalSettings.CONVERSION_RATE_FROM_DOLLAR);
            else
                price.setValue(initialPrice.getValue() / GlobalSettings.CONVERSION_RATE_FROM_DOLLAR);
        }
    }

    public String getSalePrice() {
        return price.getValue() + " " + saleCurrency.getValue();
    }

    public String getSaleQuantity() {
        return saleQuantity.getValue() + "";
    }

    public int getSaleQuantityValue() {
        return saleQuantity.getValue();
    }

    public void setSaleQuantity(int val) {
        saleQuantity.set(val);
    }

    public void incrementSaleQuantity() {
        saleQuantity.set(saleQuantity.get() + 1);
    }

    public String getDiscount() {
        return discount.getValue() + "";
    }

    public double getDiscountValue() {
        return discount.getValue();
    }

    public double getTotal() {
        return total.getValue();
    }

    public StringProperty getTotalWithCurrencyProperty() {
        return totalWithCurrency;
    }

    public StringProperty getPriceWithCurrencyProperty() {
        return priceWithCurrency;
    }

    public void setDiscount(double val) {
        discount.setValue(val);
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