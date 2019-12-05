package com.libanminds.models;

public class GraphPoint {
    private double numberValue;
    private String categoryValue;

    public GraphPoint(double numberValue, String categoryValue) {
        this.numberValue = numberValue;
        this.categoryValue = categoryValue;
    }

    public double getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(double numberValue) {
        this.numberValue = numberValue;
    }

    public String getCategoryValue() {
        return categoryValue;
    }

    public void setCategoryValue(String categoryValue) {
        this.categoryValue = categoryValue;
    }

}
