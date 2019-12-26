package com.libanminds.models.reports_models;

public class CompactReportItem {

    int code, quantitySold, quantityReturned;
    String name;

    public CompactReportItem(int code, int quantitySold, int quantityReturned, String name) {
        this.code = code;
        this.quantitySold = quantitySold;
        this.quantityReturned = quantityReturned;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public int getQuantityReturned() {
        return quantityReturned;
    }

    public void setQuantityReturned(int quantityReturned) {
        this.quantityReturned = quantityReturned;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name + "; Quantity Sold: " + quantitySold + "; Quantity Returned: " + quantityReturned;
    }
}
