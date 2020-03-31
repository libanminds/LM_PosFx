package com.libanminds.models;

import com.libanminds.constants.Constants;

public class Settings {
    public double dollarToLbp;
    public int daysCountOfSalesGraph;
    public String defaultCurrency;
    public String defaultPaymentType;

    public Settings(double dollarToLbp, int daysCountOfSalesGraph, String defaultCurrency, String defaultPaymentType) {
        this.dollarToLbp = dollarToLbp;
        this.daysCountOfSalesGraph = daysCountOfSalesGraph;
        this.defaultCurrency = defaultCurrency;
        this.defaultPaymentType = defaultPaymentType;
    }

    public Settings() {
        // Default settings for fallbacks
        this.dollarToLbp = 1500;
        this.daysCountOfSalesGraph = 7;
        this.defaultCurrency = Constants.LBP_CURRENCY;
        this.defaultPaymentType = Constants.paymentTypes[0];
    }
}
