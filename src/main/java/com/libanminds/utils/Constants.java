package com.libanminds.utils;

import com.libanminds.models.Role;

public class Constants {
    public static String ITEMS_IMAGES_FOLDER_PATH = "C:\\xampp\\htdocs\\LM_PosFx\\itemsImages\\";

    //Currencies
    public static String DOLLAR_CURRENCY = "$";
    public static String LIRA_CURRENCY = "LL";

    //Payment Types
    public static String[] paymentTypes = {"Cash", "Cheque", "Credit Card"};

    //Roles
    public static Role[] ROLES = {
            new Role(1, "Admin")
    };
}