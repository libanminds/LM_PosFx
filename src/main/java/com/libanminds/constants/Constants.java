package com.libanminds.constants;

import java.time.format.DateTimeFormatter;

public final class Constants {
    public static final String ITEMS_IMAGES_FOLDER_PATH = "itemsImages";
    public static final String DB_CONNECTION_STRING = "jdbc:sqlite:pos.db";

    //Currencies
    public static final String USD_CURRENCY = "$";
    public static final String LBP_CURRENCY = "LL";

    //Payment Types
    public static final String[] paymentTypes = {"Cash", "Cheque", "Credit Card"};

    // CSS
    public static final String CSS_ERROR_HIGHLIGHT = "errorHighlight";
    public static final String CSS_MAIN_BTN = "main-btn";
    public static final String CSS_RED_BTN = "red-btn";
    public static final String CSS_GREEN_BTN = "green-btn";
    public static final String CSS_BTN_WITH_MIN_WIDTH = "btn-with-min-width";
    public static final String MAIN_COLOR = "5580aa";

    // Formatters
    public static final DateTimeFormatter REPORT_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    private Constants() {
        throw new AssertionError("You are not allowed to instantiate this class");
    }
}