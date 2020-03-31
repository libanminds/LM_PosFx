package com.libanminds.constants;

public final class AuthorizationKeys {
    //CUSTOMERS
    public static final String VIEW_CUSTOMERS = "can_view_customers";
    public static final String ADD_CUSTOMER = "can_add_customer";
    public static final String EDIT_CUSTOMER = "can_edit_customer";
    public static final String DELETE_CUSTOMER = "can_delete_customer";
    public static final String VIEW_CUSTOMER_STATEMENT_OF_ACCOUNT = "can_view_customer_statement_of_account";

    //Suppliers
    public static final String VIEW_SUPPLIERS = "can_view_suppliers";
    public static final String ADD_SUPPLIER = "can_add_supplier";
    public static final String EDIT_SUPPLIER = "can_edit_supplier";
    public static final String DELETE_SUPPLIER = "can_delete_supplier";
    public static final String VIEW_SUPPLIER_STATEMENT_OF_ACCOUNT = "can_view_supplier_statement_of_account";

    //Items
    public static final String VIEW_ITEMS = "can_view_items";
    public static final String ADD_ITEMS = "can_add_items";
    public static final String EDIT_ITEMS = "can_edit_items";
    public static final String DELETE_ITEMS = "can_delete_items";
    public static final String COUNT_ITEMS_INVENTORY = "can_count_items_inventory";

    // Items Categories
    public static final String VIEW_ITEMS_CATEGORIES = "can_view_items_categories";
    public static final String ADD_ITEMS_CATEGORIES = "can_add_items_categories";
    public static final String EDIT_ITEMS_CATEGORIES = "can_edit_items_categories";
    public static final String DELETE_ITEMS_CATEGORIES = "can_delete_items_categories";

    //Sales
    public static final String VIEW_SALES = "can_view_sales";
    public static final String CREATE_SALE = "can_create_sale";
    public static final String VIEW_SALE = "can_view_sale";
    public static final String RETURN_SALE_ITEMS = "can_return_sale_items";
    public static final String COMPLETE_SALE_PAYMENT = "can_complete_sale_payment";

    //Purchases
    public static final String VIEW_PURCHASES = "can_view_purchases";
    public static final String CREATE_PURCHASE = "can_create_purchase";
    public static final String VIEW_PURCHASE = "can_view_purchase";
    public static final String RETURN_PURCHASE_ITEMS = "can_return_purchase_items";
    public static final String COMPLETE_PURCHASE_PAYMENT = "can_complete_purchase_payment";

    //Expenses
    public static final String VIEW_EXPENSES = "can_view_expenses";
    public static final String ADD_EXPENSES = "can_add_expenses";
    public static final String EDIT_EXPENSES = "can_edit_expenses";
    public static final String DELETE_EXPENSES = "can_delete_expenses";

    // Expenses Categories
    public static final String VIEW_EXPENSES_CATEGORIES = "can_view_expenses_categories";
    public static final String ADD_EXPENSES_CATEGORIES = "can_add_expenses_categories";
    public static final String EDIT_EXPENSES_CATEGORIES = "can_edit_expenses_categories";
    public static final String DELETE_EXPENSES_CATEGORIES = "can_delete_expenses_categories";

    //Incomes
    public static final String VIEW_INCOMES = "can_view_incomes";
    public static final String ADD_INCOME = "can_add_income";
    public static final String EDIT_INCOME = "can_edit_income";
    public static final String DELETE_INCOME = "can_delete_income";

    // Incomes Categories
    public static final String VIEW_INCOMES_CATEGORIES = "can_view_incomes_categories";
    public static final String ADD_INCOMES_CATEGORIES = "can_add_incomes_categories";
    public static final String EDIT_INCOMES_CATEGORIES = "can_edit_incomes_categories";
    public static final String DELETE_INCOMES_CATEGORIES = "can_delete_incomes_categories";

    //Users
    public static final String VIEW_USERS = "can_view_users";
    public static final String ADD_USER = "can_add_user";
    public static final String EDIT_USER = "can_edit_user";
    public static final String DELETE_USER = "can_delete_user";

    // Others
    public static final String VIEW_DASHBOARD = "can_view_dashboard";
    public static final String VIEW_REPORTS = "can_view_reports";
    public static final String VIEW_SETTINGS = "can_view_settings";

    private AuthorizationKeys() {
        throw new AssertionError("You are not allowed to instantiate this class");
    }
}
