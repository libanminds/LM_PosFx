package com.libanminds.constants;

public final class Views {
    // Root
    public static final String LOGIN = "/views/login.fxml";
    public static final String MAIN = "/views/main.fxml";

    // Main
    public static final String CUSTOMERS = "/views/main/customers.fxml";
    public static final String DASHBOARD = "/views/main/dashboard.fxml";
    public static final String EMPLOYEES = "/views/main/employees.fxml";
    public static final String EXPENSES = "/views/main/expenses.fxml";
    public static final String INCOME = "/views/main/income.fxml";
    public static final String ITEMS = "/views/main/items.fxml";
    public static final String NEW_PURCHASE = "/views/main/new_purchase.fxml";
    public static final String NEW_SALE = "/views/main/new_sale.fxml";
    public static final String PURCHASES = "/views/main/purchases.fxml";
    public static final String REPORTS = "/views/main/reports.fxml";
    public static final String SALES = "/views/main/sales.fxml";
    public static final String SETTINGS = "/views/main/settings.fxml";
    public static final String SUPPLIERS = "/views/main/suppliers.fxml";

    // Dialogs
    public static final String ADD_CAR = "/views/dialogs/add_car.fxml";
    public static final String ADD_CUSTOMER = "/views/dialogs/add_customer.fxml";
    public static final String ADD_EMPLOYEE = "/views/dialogs/add_employee.fxml";
    public static final String ADD_EXPENSE = "/views/dialogs/add_expense.fxml";
    public static final String ADD_EXPENSE_TYPE = "/views/dialogs/add_expense_type.fxml";
    public static final String ADD_INCOME = "/views/dialogs/add_income.fxml";
    public static final String ADD_INCOME_TYPE = "/views/dialogs/add_income_type.fxml";
    public static final String ADD_ITEM = "/views/dialogs/add_item.fxml";
    public static final String ADD_ITEM_CATEGORY = "/views/dialogs/add_item_category.fxml";
    public static final String ADD_SUPPLIER = "/views/dialogs/add_supplier.fxml";
    public static final String COMPLETE_PURCHASE = "/views/dialogs/complete_purchase.fxml";
    public static final String COMPLETE_SALE = "/views/dialogs/complete_sale.fxml";
    public static final String RETURN_PURCHASE_ITEMS = "/views/dialogs/return_purchase_items.fxml";
    public static final String RETURN_SALE_ITEMS = "/views/dialogs/return_sale_items.fxml";
    public static final String SELECT_CAR = "/views/dialogs/select_car.fxml";
    public static final String SELECT_CUSTOMER = "/views/dialogs/select_customer.fxml";
    public static final String SELECT_ITEM = "/views/dialogs/select_item.fxml";
    public static final String SELECT_SUPPLIER = "/views/dialogs/select_supplier.fxml";
    public static final String VIEW_PURCHASE = "/views/dialogs/view_purchase.fxml";
    public static final String VIEW_SALE = "/views/dialogs/view_sale.fxml";

    // Transactions History
    public static final String CUSTOMER_STATEMENT_OF_ACCOUNT = "/views/dialogs/customer_statement_of_account.fxml";
    public static final String SUPPLIER_STATEMENT_OF_ACCOUNT = "/views/dialogs/supplier_statement_of_account.fxml";

    // Categories Controllers
    public static final String ITEMS_CATEGORIES = "/views/main/item_categories.fxml";
    public static final String INCOME_TYPES = "/views/main/income_types.fxml";
    public static final String EXPENSE_TYPES = "/views/main/expense_types.fxml";

    // Reports advanced options
    public static final String CUSTOMERS_ADVANCED_OPTIONS = "/views/advanced_options/customers_advanced_options.fxml";
    public static final String EXPENSES_ADVANCED_OPTIONS = "/views/advanced_options/expenses_advanced_options.fxml";
    public static final String INCOME_ADVANCED_OPTIONS = "/views/advanced_options/income_advanced_options.fxml";
    public static final String PURCHASED_ITEMS_ADVANCED_OPTIONS = "/views/advanced_options/purchased_items_advanced_options.fxml";
    public static final String PURCHASES_ADVANCED_OPTIONS = "/views/advanced_options/purchases_advanced_options.fxml";
    public static final String SALES_ADVANCED_OPTIONS = "/views/advanced_options/sales_advanced_options.fxml";
    public static final String SOLD_ITEMS_ADVANCED_OPTIONS = "/views/advanced_options/sold_items_advanced_options.fxml";
    public static final String SUPPLIERS_ADVANCED_OPTIONS = "/views/advanced_options/suppliers_advanced_options.fxml";

    private Views() {
        throw new AssertionError("You are not allowed to instantiate this class");
    }
}
