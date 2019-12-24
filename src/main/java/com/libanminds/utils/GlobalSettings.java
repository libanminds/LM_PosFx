package com.libanminds.utils;

import com.libanminds.models.Role;

public class GlobalSettings {

    //TODO: retrieve these from the settings.
    public static double CONVERSION_RATE_FROM_DOLLAR = 1500;
    public static int DAYS_COUNT_OF_SALES_GRAPH = 7;

    public static String DEFAULT_CURRENCY = Constants.LIRA_CURRENCY;
    public static String DEFAULT_PAYMENT_TYPE = Constants.paymentTypes[0];
    public static Role DEFAULT_USER_ROLE = Constants.ROLES[0];
}
