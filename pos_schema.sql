BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "user_prefs" (
	"id"	INTEGER NOT NULL,
	"property"	TEXT NOT NULL,
	"value"	TEXT NOT NULL,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "receivings" (
	"id"	INTEGER NOT NULL,
	"supplier_id"	INTEGER NOT NULL,
	"currency"	TEXT NOT NULL,
	"conversion_rate"	INTEGER,
	"is_official"	INTEGER NOT NULL DEFAULT 1,
	"discount"	INTEGER NOT NULL DEFAULT 0,
	PRIMARY KEY("id"),
	FOREIGN KEY("supplier_id") REFERENCES "suppliers"("id")
);
CREATE TABLE IF NOT EXISTS "sales" (
	"id"	INTEGER NOT NULL,
	"customer_id"	INTEGER NOT NULL,
	"discount"	INTEGER NOT NULL DEFAULT 0,
	"tax_id"	INTEGER,
	"conversion_rate"	INTEGER,
	"currency"	TEXT NOT NULL,
	"is_official"	INTEGER NOT NULL DEFAULT 1,
	PRIMARY KEY("id"),
	FOREIGN KEY("customer_id") REFERENCES "customers"("id"),
	FOREIGN KEY("tax_id") REFERENCES "tax"("id")
);
CREATE TABLE IF NOT EXISTS "suppliers" (
	"id"	INTEGER NOT NULL,
	"first_name"	TEXT NOT NULL,
	"last_name"	TEXT NOT NULL,
	"email"	TEXT,
	"phone"	TEXT,
	"company"	TEXT,
	"address"	TEXT,
	"notes"	TEXT,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "tax" (
	"id"	INTEGER NOT NULL,
	"name"	TEXT NOT NULL,
	"percentage"	INTEGER NOT NULL,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "sale_cars" (
	"id"	INTEGER NOT NULL,
	"car_id"	INTEGER NOT NULL,
	"sale_id"	INTEGER NOT NULL,
	PRIMARY KEY("id"),
	FOREIGN KEY("sale_id") REFERENCES "sales"("id"),
	FOREIGN KEY("car_id") REFERENCES "cars"("id")
);
CREATE TABLE IF NOT EXISTS "sale_items" (
	"id"	INTEGER NOT NULL,
	"item_id"	INTEGER NOT NULL,
	"sale_id"	INTEGER NOT NULL,
	"quantity"	INTEGER NOT NULL DEFAULT 1,
	"discount"	INTEGER NOT NULL DEFAULT 0,
	PRIMARY KEY("id"),
	FOREIGN KEY("sale_id") REFERENCES "sales"("id"),
	FOREIGN KEY("item_id") REFERENCES "items"("id")
);
CREATE TABLE IF NOT EXISTS "receiving_items" (
	"id"	INTEGER NOT NULL,
	"item_id"	INTEGER NOT NULL,
	"receiving_id"	INTEGER NOT NULL,
	"quantity"	INTEGER NOT NULL DEFAULT 1,
	"discount"	INTEGER NOT NULL DEFAULT 0,
	PRIMARY KEY("id"),
	FOREIGN KEY("item_id") REFERENCES "items"("id"),
	FOREIGN KEY("receiving_id") REFERENCES "receivings"("id")
);
CREATE TABLE IF NOT EXISTS "items" (
	"id"	INTEGER NOT NULL,
	"code"	TEXT NOT NULL UNIQUE,
	"image"	TEXT,
	"name"	TEXT NOT NULL,
	"category_id"	INTEGER,
	"cost"	INTEGER NOT NULL DEFAULT 0,
	"price"	INTEGER NOT NULL DEFAULT 0,
	"currency"	TEXT NOT NULL,
	"quantity"	INTEGER NOT NULL DEFAULT 0,
	"description"	TEXT,
	"min_stock"	INTEGER,
	"created_at"	TEXT,
	"updated_at"	TEXT,
	PRIMARY KEY("id"),
	FOREIGN KEY("category_id") REFERENCES "item_categories"("id")
);
CREATE TABLE IF NOT EXISTS "item_categories" (
	"id"	INTEGER NOT NULL,
	"name"	TEXT NOT NULL,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "incomes" (
	"id"	INTEGER NOT NULL,
	"type_id"	INTEGER NOT NULL,
	"name"	TEXT NOT NULL,
	"amount"	INTEGER NOT NULL,
	"currency"	TEXT NOT NULL,
	"notes"	INTEGER,
	PRIMARY KEY("id"),
	FOREIGN KEY("type_id") REFERENCES "income_types"("id")
);
CREATE TABLE IF NOT EXISTS "expenses" (
	"id"	INTEGER NOT NULL,
	"type_id"	INTEGER NOT NULL,
	"name"	TEXT NOT NULL,
	"amount"	INTEGER NOT NULL,
	"currency"	TEXT NOT NULL,
	"notes"	TEXT,
	PRIMARY KEY("id"),
	FOREIGN KEY("type_id") REFERENCES "expense_types"("id")
);
CREATE TABLE IF NOT EXISTS "income_types" (
	"id"	INTEGER NOT NULL,
	"name"	TEXT NOT NULL,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "expense_types" (
	"id"	INTEGER NOT NULL,
	"name"	TEXT NOT NULL,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "cars" (
	"id"	INTEGER NOT NULL,
	"owner_id"	INTEGER NOT NULL,
	"model"	TEXT NOT NULL,
	"year"	INTEGER,
	"created_at"	TEXT,
	"updated_at"	TEXT,
	PRIMARY KEY("id"),
	FOREIGN KEY("owner_id") REFERENCES "customers"("id")
);
CREATE TABLE IF NOT EXISTS "customer_transactions" (
	"id"	INTEGER NOT NULL,
	"customer_id"	INTEGER NOT NULL,
	"invoice_id"	INTEGER,
	"amount"	INTEGER NOT NULL,
	"currency"	TEXT NOT NULL,
	"created_at"	TEXT,
	"updated_at"	TEXT,
	PRIMARY KEY("id"),
	FOREIGN KEY("invoice_id") REFERENCES "sales"("id"),
	FOREIGN KEY("customer_id") REFERENCES "customers"("id")
);
CREATE TABLE IF NOT EXISTS "customers" (
	"id"	INTEGER NOT NULL,
	"first_name"	TEXT NOT NULL,
	"last_name"	TEXT NOT NULL,
	"email"	TEXT,
	"phone"	TEXT,
	"address"	TEXT,
	"company"	TEXT,
	"notes"	TEXT,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "supplier_transactions" (
	"id"	INTEGER NOT NULL,
	"supplier_id"	INTEGER NOT NULL,
	"invoice_id"	INTEGER,
	"amount"	INTEGER NOT NULL,
	"currency"	TEXT NOT NULL,
	"created_at"	TEXT,
	"updated_at"	TEXT,
	PRIMARY KEY("id"),
	FOREIGN KEY("invoice_id") REFERENCES "receivings"("id")
);
CREATE TABLE IF NOT EXISTS "users" (
	"id"	INTEGER NOT NULL,
	"name"	TEXT NOT NULL,
	"username"	TEXT NOT NULL,
	"password"	TEXT NOT NULL,
	"role"	TEXT NOT NULL,
	PRIMARY KEY("id")
);
COMMIT;
