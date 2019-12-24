package com.libanminds.utils;

import com.libanminds.models.Role;

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

    // CSS classes
    public static final String CSS_ERROR_HIGHLIGHT = "errorHighlight";

    private Constants() {
        throw new AssertionError("You are not allowed to instantiate this class");
    }
}