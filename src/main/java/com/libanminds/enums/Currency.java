package com.libanminds.enums;

import java.util.ArrayList;
import java.util.List;

public enum Currency {
    USD("usd", "$"),
    LBP("lbp", "L.L.");

    private String val;
    private String symbol;

    Currency(String val, String symbol) {
        this.val = val;
        this.symbol = symbol;
    }

    public String getVal() {
        return val;
    }

    public String getSymbol() {
        return symbol;
    }

    public static List<String> getSymbols() {
        List<String> result = new ArrayList<>();
        for (Currency c : Currency.values()) {
            result.add(c.getSymbol());
        }
        return result;
    }

    public static Currency fromString(String s) {
        switch (s) {
            case "lbp":
                return LBP;
            case "usd":
                return USD;
            default:
                return LBP;
        }
    }
}
