package com.libanminds.utils;

public final class Views {
    public static final String LOGIN = "/views/login.fxml";
    public static final String MAIN = "/views/main.fxml";
    public static final String DASHBOARD = "/views/main/dashboard.fxml";
    public static final String CUSTOMERS = "/views/main/customers.fxml";
    public static final String SUPPLIERS = "/views/main/suppliers.fxml";
    public static final String ITEMS = "/views/main/items.fxml";
    public static final String REPORTS = "/views/main/reports.fxml";
    public static final String SALES = "/views/main/sales.fxml";
    public static final String RECEIVING = "/views/main/receiving.fxml";
    public static final String EXPENSES = "/views/main/expenses.fxml";
    public static final String INCOME = "/views/main/income.fxml";
//    public static final String APPOINTMENTS = "/views/main/appointments.fxml";
    public static final String EMPLOYEES = "/views/main/employees.fxml";
    public static final String SETTINGS = "/views/main/settings.fxml";

    //Dialogs
    public static final String ADD_CUSTOMER = "/views/dialogs/add_customer.fxml";
    public static final String ADD_SUPPLIER = "/views/dialogs/add_supplier.fxml";

    private Views(){
        throw new AssertionError("You are not allowed to instantiate this class");
    }
}
