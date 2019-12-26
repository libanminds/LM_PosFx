package com.libanminds.utils;

import com.libanminds.models.Role;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final String ITEMS_IMAGES_FOLDER_PATH = "C:\\xampp\\htdocs\\LM_PosFx\\itemsImages\\";

    //Currencies
    public static final String DOLLAR_CURRENCY = "$";
    public static final String LIRA_CURRENCY = "LL";

    //Payment Types
    public static String[] paymentTypes = {"Cash", "Cheque", "Credit Card"};

    //Roles
    public static Role[] ROLES = {
            new Role(1, "Admin")
    };

    // CSS
    public static final String CSS_ERROR_HIGHLIGHT = "errorHighlight";
    public static final String MAIN_COLOR = "5580aa";

    // Formatters
    public  static final DateTimeFormatter REPORT_DATE_FORMAT =  DateTimeFormatter.ISO_LOCAL_DATE;

    private Constants() {
        throw new AssertionError("You are not allowed to instantiate this class");
    }
}