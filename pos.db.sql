BEGIN TRANSACTION;
DROP TABLE IF EXISTS "suppliers";
CREATE TABLE IF NOT EXISTS "suppliers" (
	"id"	INTEGER NOT NULL,
	"first_name"	TEXT NOT NULL,
	"last_name"	TEXT,
	"email"	TEXT,
	"phone"	TEXT,
	"company"	TEXT,
	"address"	TEXT,
	"notes"	TEXT,
	"balance"	REAL,
	"created_at"	TEXT DEFAULT CURRENT_TIMESTAMP,
	"updated_at"	TEXT,
	PRIMARY KEY("id")
);
DROP TABLE IF EXISTS "supplier_transactions";
CREATE TABLE IF NOT EXISTS "supplier_transactions" (
	"id"	INTEGER NOT NULL,
	"supplier_id"	INTEGER NOT NULL,
	"invoice_id"	INTEGER NOT NULL,
	"amount"	INTEGER NOT NULL,
	"currency"	INTEGER NOT NULL,
	"is_refund"	INTEGER NOT NULL,
	"created_at"	TEXT DEFAULT CURRENT_TIMESTAMP,
	"updated_at"	TEXT,
	PRIMARY KEY("id"),
	FOREIGN KEY("invoice_id") REFERENCES "purchases"("id")
);
DROP TABLE IF EXISTS "purchase_items";
CREATE TABLE IF NOT EXISTS "purchase_items" (
	"id"	INTEGER NOT NULL,
	"item_id"	INTEGER NOT NULL,
	"item_properties_id"	INTEGER NOT NULL,
	"purchase_id"	INTEGER NOT NULL,
	"quantity"	INTEGER NOT NULL DEFAULT 1,
	"discount"	INTEGER NOT NULL DEFAULT 0,
	"returned_quantity"	INTEGER DEFAULT 0,
	"created_at"	TEXT DEFAULT CURRENT_TIMESTAMP,
	"updated_at"	TEXT,
	PRIMARY KEY("id"),
	FOREIGN KEY("item_id") REFERENCES "items"("id"),
	FOREIGN KEY("purchase_id") REFERENCES "purchases"("id")
);
DROP TABLE IF EXISTS "items";
CREATE TABLE IF NOT EXISTS "items" (
	"id"	INTEGER NOT NULL,
	"code"	TEXT NOT NULL UNIQUE,
	"item_properties_id"	INTEGER NOT NULL,
	"image"	TEXT,
	"name"	TEXT NOT NULL,
	"category_id"	INTEGER NOT NULL,
	"quantity"	INTEGER NOT NULL DEFAULT 0,
	"description"	TEXT,
	"min_stock"	INTEGER NOT NULL,
	"ttc"	INTEGER NOT NULL DEFAULT 0,
	"created_at"	TEXT DEFAULT CURRENT_TIMESTAMP,
	"updated_at"	TEXT,
	PRIMARY KEY("id"),
	FOREIGN KEY("category_id") REFERENCES "item_categories"("id")
);
DROP TABLE IF EXISTS "item_properties";
CREATE TABLE IF NOT EXISTS "item_properties" (
	"id"	INTEGER NOT NULL,
	"cost"	REAL NOT NULL,
	"price"	REAL NOT NULL,
	"currency"	TEXT NOT NULL,
	"created_at"	TEXT DEFAULT CURRENT_TIMESTAMP,
	"updated_at"	TEXT,
	PRIMARY KEY("id")
);
DROP TABLE IF EXISTS "customers";
CREATE TABLE IF NOT EXISTS "customers" (
	"id"	INTEGER NOT NULL,
	"first_name"	TEXT NOT NULL,
	"last_name"	TEXT,
	"email"	TEXT,
	"phone"	TEXT,
	"address"	TEXT,
	"company"	TEXT,
	"balance"	REAL,
	"notes"	INTEGER,
	"created_at"	TEXT DEFAULT CURRENT_TIMESTAMP,
	"updated_at"	TEXT,
	PRIMARY KEY("id")
);
DROP TABLE IF EXISTS "permissions";
CREATE TABLE IF NOT EXISTS "permissions" (
	"id"	INTEGER NOT NULL,
	"role_id"	INTEGER NOT NULL,
	"permission"	INTEGER,
	PRIMARY KEY("id")
);
DROP TABLE IF EXISTS "users";
CREATE TABLE IF NOT EXISTS "users" (
	"id"	INTEGER NOT NULL,
	"username"	TEXT NOT NULL UNIQUE,
	"first_name"	TEXT NOT NULL,
	"last_name"	TEXT,
	"password"	TEXT NOT NULL,
	"email"	TEXT,
	"phone"	TEXT,
	"role_id"	INTEGER NOT NULL,
	"address"	TEXT,
	PRIMARY KEY("id")
);
DROP TABLE IF EXISTS "role";
CREATE TABLE IF NOT EXISTS "role" (
	"id"	INTEGER NOT NULL,
	"name"	TEXT NOT NULL,
	PRIMARY KEY("id")
);
DROP TABLE IF EXISTS "customer_transactions";
CREATE TABLE IF NOT EXISTS "customer_transactions" (
	"id"	INTEGER NOT NULL,
	"customer_id"	INTEGER NOT NULL,
	"invoice_id"	INTEGER NOT NULL,
	"amount"	INTEGER NOT NULL,
	"currency"	INTEGER NOT NULL,
	"is_refund"	INTEGER NOT NULL,
	"created_at"	TEXT DEFAULT CURRENT_TIMESTAMP,
	"updated_at"	TEXT,
	PRIMARY KEY("id"),
	FOREIGN KEY("invoice_id") REFERENCES "sales"("id"),
	FOREIGN KEY("customer_id") REFERENCES "customers"("id")
);
DROP TABLE IF EXISTS "purchases";
CREATE TABLE IF NOT EXISTS "purchases" (
	"id"	INTEGER NOT NULL,
	"supplier_id"	INTEGER NOT NULL,
	"discount"	REAL NOT NULL DEFAULT 0,
	"tax_id"	INTEGER NOT NULL,
	"conversion_rate"	REAL,
	"total_amount"	REAL NOT NULL,
	"currency"	TEXT NOT NULL,
	"paid_amount"	REAL NOT NULL,
	"is_official"	INTEGER NOT NULL DEFAULT 1,
	"type"	TEXT NOT NULL DEFAULT 'cash',
	"created_at"	TEXT DEFAULT CURRENT_TIMESTAMP,
	"updated_at"	TEXT,
	PRIMARY KEY("id"),
	FOREIGN KEY("supplier_id") REFERENCES "suppliers"("id")
);
DROP TABLE IF EXISTS "expenses";
CREATE TABLE IF NOT EXISTS "expenses" (
	"id"	INTEGER NOT NULL,
	"type_id"	INTEGER NOT NULL,
	"description"	TEXT NOT NULL,
	"amount"	REAL NOT NULL,
	"currency"	TEXT NOT NULL,
	"payment_type"	TEXT NOT NULL,
	"recipient"	TEXT,
	"notes"	TEXT,
	"created_at"	TEXT DEFAULT CURRENT_TIMESTAMP,
	"updated_at"	TEXT,
	PRIMARY KEY("id"),
	FOREIGN KEY("type_id") REFERENCES "expense_types"("id")
);
DROP TABLE IF EXISTS "sale_items";
CREATE TABLE IF NOT EXISTS "sale_items" (
	"id"	INTEGER NOT NULL,
	"item_id"	INTEGER NOT NULL,
	"item_properties_id"	INTEGER NOT NULL,
	"sale_id"	INTEGER NOT NULL,
	"quantity"	INTEGER NOT NULL DEFAULT 1,
	"discount"	REAL NOT NULL DEFAULT 0,
	"returned_quantity"	INTEGER DEFAULT 0,
	"created_at"	TEXT DEFAULT CURRENT_TIMESTAMP,
	"updated_at"	TEXT,
	PRIMARY KEY("id"),
	FOREIGN KEY("sale_id") REFERENCES "sales"("id"),
	FOREIGN KEY("item_id") REFERENCES "items"("id")
);
DROP TABLE IF EXISTS "incomes";
CREATE TABLE IF NOT EXISTS "incomes" (
	"id"	INTEGER NOT NULL,
	"type_id"	INTEGER NOT NULL,
	"description"	TEXT NOT NULL,
	"amount"	REAL NOT NULL,
	"currency"	TEXT NOT NULL,
	"payment_type"	TEXT,
	"taken_from"	TEXT,
	"notes"	TEXT,
	"created_at"	TEXT DEFAULT CURRENT_TIMESTAMP,
	"updated_at"	TEXT,
	PRIMARY KEY("id"),
	FOREIGN KEY("type_id") REFERENCES "income_types"("id")
);
DROP TABLE IF EXISTS "sales";
CREATE TABLE IF NOT EXISTS "sales" (
	"id"	INTEGER NOT NULL,
	"customer_id"	INTEGER NOT NULL,
	"discount"	REAL NOT NULL DEFAULT 0,
	"tax_id"	INTEGER,
	"conversion_rate"	REAL,
	"total_amount"	BLOB,
	"currency"	TEXT NOT NULL,
	"paid_amount"	REAL NOT NULL,
	"is_official"	INTEGER NOT NULL DEFAULT 1,
	"type"	TEXT NOT NULL DEFAULT 'cash',
	"created_at"	TEXT DEFAULT CURRENT_TIMESTAMP,
	"updated_at"	TEXT,
	PRIMARY KEY("id"),
	FOREIGN KEY("customer_id") REFERENCES "customers"("id"),
	FOREIGN KEY("tax_id") REFERENCES "tax"("id")
);
DROP TABLE IF EXISTS "item_property_history";
CREATE TABLE IF NOT EXISTS "item_property_history" (
	"item_id"	INTEGER NOT NULL,
	"property_id"	INTEGER NOT NULL
);
DROP TABLE IF EXISTS "user_prefs";
CREATE TABLE IF NOT EXISTS "user_prefs" (
	"id"	INTEGER NOT NULL,
	"property"	TEXT NOT NULL,
	"value"	TEXT NOT NULL,
	PRIMARY KEY("id")
);
DROP TABLE IF EXISTS "tax";
CREATE TABLE IF NOT EXISTS "tax" (
	"id"	INTEGER NOT NULL,
	"name"	TEXT NOT NULL,
	"percentage"	INTEGER NOT NULL,
	PRIMARY KEY("id")
);
DROP TABLE IF EXISTS "sale_cars";
CREATE TABLE IF NOT EXISTS "sale_cars" (
	"id"	INTEGER NOT NULL,
	"car_id"	INTEGER NOT NULL,
	"sale_id"	INTEGER NOT NULL,
	PRIMARY KEY("id"),
	FOREIGN KEY("sale_id") REFERENCES "sales"("id"),
	FOREIGN KEY("car_id") REFERENCES "cars"("id")
);
DROP TABLE IF EXISTS "item_categories";
CREATE TABLE IF NOT EXISTS "item_categories" (
	"id"	INTEGER NOT NULL,
	"name"	TEXT NOT NULL,
	PRIMARY KEY("id")
);
DROP TABLE IF EXISTS "income_types";
CREATE TABLE IF NOT EXISTS "income_types" (
	"id"	INTEGER NOT NULL,
	"name"	TEXT NOT NULL,
	PRIMARY KEY("id")
);
DROP TABLE IF EXISTS "expense_types";
CREATE TABLE IF NOT EXISTS "expense_types" (
	"id"	INTEGER NOT NULL,
	"name"	TEXT NOT NULL,
	PRIMARY KEY("id")
);
DROP TABLE IF EXISTS "cars";
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
INSERT INTO "suppliers" ("id","first_name","last_name","email","phone","company","address","notes","balance","created_at","updated_at") VALUES (1,'Elon','Musk','elon@tesla.com','0033423423 432423 432','Tesla','USA','I also build SpaceX',1500.0,NULL,NULL),
 (2,'AddSupplier','Test','Test','Test','','','',7000.0,NULL,NULL),
 (3,'Bassel','Cheaib','bassel@gmail.com','8003324324','Libanminds','Sultan Center','no comments',NULL,NULL,NULL);
INSERT INTO "supplier_transactions" ("id","supplier_id","invoice_id","amount","currency","is_refund","created_at","updated_at") VALUES (1,1,7,1500,'LL',0,NULL,NULL),
 (2,1,8,2,'$',0,NULL,NULL),
 (3,1,8,1.33,'$',0,NULL,NULL),
 (4,8,1,3.33,'$',1,NULL,NULL),
 (5,7,1,1500,'LL',1,NULL,NULL),
 (6,1,1,7000,'LL',1,NULL,NULL),
 (7,1,3,4000,'LL',0,NULL,NULL),
 (8,3,1,4000,'LL',1,NULL,NULL),
 (9,1,9,15,'$',0,NULL,NULL),
 (10,1,9,5,'$',0,NULL,NULL),
 (11,1,9,1,'$',1,NULL,NULL),
 (12,1,4,5000,'LL',0,NULL,NULL),
 (13,1,4,1000,'LL',0,NULL,NULL);
INSERT INTO "purchase_items" ("id","item_id","item_properties_id","purchase_id","quantity","discount","returned_quantity","created_at","updated_at") VALUES (1,3,5,1,1,0,1,NULL,NULL),
 (2,3,5,2,1,0,0,NULL,NULL),
 (3,4,7,3,1,0,1,NULL,NULL),
 (4,3,5,4,1,0,0,NULL,NULL),
 (5,2,3,6,1,0,1,NULL,NULL),
 (6,1,2,7,1,0,1,NULL,NULL),
 (7,2,3,8,1,0,1,NULL,NULL),
 (8,1,2,9,1,0,1,NULL,NULL),
 (9,2,3,9,6,0,0,NULL,NULL),
 (10,1,2,10,5,0,0,NULL,NULL),
 (11,1,2,11,8,0,0,NULL,NULL),
 (12,1,2,12,1,0,1,NULL,NULL);
INSERT INTO "items" ("id","code","item_properties_id","image","name","category_id","quantity","description","min_stock","ttc","created_at","updated_at") VALUES (1,'1',2,'','Banana',2,3,'Can be eaten',5,0,NULL,NULL),
 (2,'2',3,'','Strawberry',2,53,'Non',20,0,NULL,NULL),
 (3,'3',5,'','Apple',2,893,'Over Pricing',50,0,NULL,NULL),
 (4,'4',7,'','Orange',2,26,'can be eaten',25,0,NULL,NULL);
INSERT INTO "item_properties" ("id","cost","price","currency","created_at","updated_at") VALUES (1,1000.0,1500.0,'LL',NULL,NULL),
 (2,1000.0,1500.0,'LL',NULL,NULL),
 (3,3000.0,5000.0,'LL',NULL,NULL),
 (4,1000.0,7000.0,'LL',NULL,NULL),
 (5,1000.0,7000.0,'LL',NULL,NULL),
 (6,2.0,3.0,'$',NULL,NULL),
 (7,2.0,3.0,'$',NULL,NULL);
INSERT INTO "customers" ("id","first_name","last_name","email","phone","address","company","balance","notes","created_at","updated_at") VALUES (1,'Ahmad','Sannan','ahmad@libanminds.com','07 000 008','Akbieh','Libanminds',7500.0,'This is a note',NULL,NULL),
 (3,'sannana','fdsf','dsafsadf','sadfsdafsa','sdfsdfa','fsdafs',4500.0,'adfsdaf',NULL,NULL),
 (4,'HELENnn','HELEN','foaunpo','fao[fna[o','v[aondfo;a','fadsnf[asn',NULL,'vfao[dsnf',NULL,NULL),
 (5,'jjj','jj','jj','jj','jj','jj',NULL,'jjj',NULL,NULL),
 (6,'fsdf','fasd','fsadfsd','fsdf','fasdf','sdf',25000.0,'sdfsda',NULL,NULL),
 (7,'Ahmad','','','','','',NULL,'',NULL,NULL),
 (8,'fsadfasd','fasdfasdf','fasd','fasdfasdf','sdfasd','asdfasdf',NULL,'asdfasfs',NULL,NULL),
 (9,'fsadfasdf','','','','asdfasdfasdfasdfasd','',NULL,'',NULL,NULL);
INSERT INTO "permissions" ("id","role_id","permission") VALUES (1,1,'can_view_dashboard'),
 (2,2,'can_view_customers'),
 (3,1,'can_view_customers'),
 (4,1,'can_view_suppliers'),
 (5,1,'can_view_items'),
 (6,1,'can_view_reports'),
 (7,1,'can_view_sales'),
 (8,1,'can_create_sales'),
 (9,1,'can_view_purchases'),
 (10,1,'can_create_purchase'),
 (11,1,'can_view_expenses'),
 (12,1,'can_view_incomes'),
 (13,1,'can_view_users'),
 (14,1,'can_view_settings'),
 (15,1,'can_view_customer_statement_of_account'),
 (16,1,'can_add_customer'),
 (17,1,'can_edit_customer'),
 (18,1,'can_delete_customer'),
 (19,1,'can_view_supplier_statement_of_account'),
 (20,1,'can_add_supplier'),
 (21,1,'can_edit_supplier'),
 (22,1,'can_delete_supplier'),
 (23,1,'can_add_expenses'),
 (24,1,'can_edit_expenses'),
 (25,1,'can_delete_expenses'),
 (26,1,'can_add_income'),
 (27,1,'can_edit_income'),
 (28,1,'can_delete_income'),
 (29,1,'can_add_user'),
 (30,1,'can_edit_user'),
 (31,1,'can_delete_user'),
 (32,1,'can_add_items'),
 (33,1,'can_edit_items'),
 (34,1,'can_delete_items'),
 (35,1,'can_view_sale'),
 (36,1,'can_return_sale_items'),
 (37,1,'can_complete_sale_payment'),
 (38,1,'can_create_sale'),
 (39,1,'can_view_purchase'),
 (40,1,'can_return_purchase_items'),
 (41,1,'can_complete_purchase_payment'),
 (42,1,'can_view_items_categories'),
 (43,1,'can_count_items_inventory'),
 (44,1,'can_view_expenses_categories'),
 (45,1,'can_view_incomes_categories'),
 (46,1,'can_add_items_categories'),
 (47,1,'can_edit_items_categories'),
 (48,1,'can_delete_items_categories'),
 (49,1,'can_add_expenses_categories'),
 (50,1,'can_edit_expenses_categories'),
 (51,1,'can_delete_expenses_categories'),
 (52,1,'can_add_incomes_categories'),
 (53,1,'can_edit_incomes_categories'),
 (54,1,'can_delete_incomes_categories');
INSERT INTO "users" ("id","username","first_name","last_name","password","email","phone","role_id","address") VALUES (1,'ahmadsannan','Ahmad','Sannan','123456','','',1,''),
 (2,'asdfsafsdfs','dfsdf','fasdfasd','fasdf','sdfasd','asdf',2,'asdfasdf');
INSERT INTO "role" ("id","name") VALUES (1,'admin'),
 (2,'cachier');
INSERT INTO "customer_transactions" ("id","customer_id","invoice_id","amount","currency","is_refund","created_at","updated_at") VALUES (2,1,18,500,'LL',0,'2019-12-09 17:41:03',NULL),
 (3,1,17,35000,'LL',0,'2019-12-09 17:48:08',NULL),
 (4,1,18,1000,'LL',0,'2019-12-09 17:55:09',NULL),
 (5,1,1,10000,'LL',1,'2019-12-09 18:04:04',NULL),
 (6,1,20,5000,'LL',0,'2019-12-10 09:46:29',NULL),
 (7,1,21,5,'$',0,'2019-12-10 09:47:00',NULL),
 (8,1,1,3000,'LL',1,'2019-12-10 09:55:53',NULL),
 (9,1,1,2000,'LL',1,'2019-12-10 09:59:28',NULL),
 (10,1,22,25000,'LL',0,'2019-12-10 10:01:35',NULL),
 (11,1,1,10000,'LL',1,'2019-12-10 10:03:39',NULL),
 (12,1,1,5000,'LL',1,'2019-12-10 10:06:55',NULL),
 (13,1,22,5000,'LL',1,'2019-12-10 10:08:06',NULL),
 (14,1,21,3,'$',1,'2019-12-10 11:15:38',NULL),
 (15,1,23,3000,'LL',0,'2019-12-14 11:04:55',NULL),
 (16,1,23,3000,'LL',1,'2019-12-16 10:03:31',NULL),
 (17,1,21,1,'$',1,'2019-12-16 10:09:19',NULL),
 (18,1,22,5000,'LL',1,'2019-12-16 11:10:32',NULL),
 (19,1,24,50000,'LL',0,'2019-12-16 11:11:59',NULL),
 (20,1,24,1500,'LL',1,'2019-12-16 11:17:48',NULL),
 (21,1,24,9000,'LL',1,'2019-12-16 11:19:12',NULL),
 (22,1,14,30000,'LL',0,'2019-12-16 13:53:33',NULL),
 (23,1,10,25000,'LL',0,'2019-12-16 13:53:46',NULL),
 (24,1,30,30000,'LL',0,'2019-12-20 18:09:31',NULL),
 (25,1,30,5000,'LL',0,'2019-12-20 18:09:43',NULL),
 (26,1,33,3000,'LL',0,'2020-02-06 10:08:57',NULL);
INSERT INTO "purchases" ("id","supplier_id","discount","tax_id","conversion_rate","total_amount","currency","paid_amount","is_official","type","created_at","updated_at") VALUES (1,1,0.0,1,1500.0,0.0,'LL',0.0,1,'Cash','2019-12-08 15:05:37',NULL),
 (2,2,0.0,1,1500.0,7000.0,'LL',0.0,1,'Cash','2019-12-08 15:05:54',NULL),
 (3,1,0.0,1,1500.0,0.0,'LL',0.0,1,'Cash','2019-12-08 15:06:32',NULL),
 (4,1,0.0,1,1500.0,7000.0,'LL',6000.0,1,'Cash','2019-12-08 15:08:09',NULL),
 (5,1,0.0,1,1500.0,0.0,'LL',0.0,1,'Cash','2019-12-08 15:08:18',NULL),
 (6,1,0.0,1,1500.0,0.0,'LL',0.0,1,'Cash','2019-12-09 18:15:04',NULL),
 (7,1,0.0,1,1500.0,0.0,'LL',0.0,1,'Cash','2019-12-09 18:16:08',NULL),
 (8,1,0.0,1,1500.0,0.0,'$',0.0,1,'Cash','2019-12-09 18:16:56',NULL),
 (9,1,1.0,1,1500.0,19.0,'$',19.0,1,'Cash','2019-12-10 11:17:17',NULL),
 (10,1,0.0,1,1500.0,7500.0,'LL',0.0,1,'Cash','2019-12-16 14:45:46',NULL),
 (11,1,0.0,1,1500.0,12000.0,'LL',0.0,1,'Cash','2019-12-16 14:46:31',NULL),
 (12,1,0.0,1,1500.0,0.0,'LL',0.0,1,'Cash','2019-12-25 16:35:07',NULL);
INSERT INTO "expenses" ("id","type_id","description","amount","currency","payment_type","recipient","notes","created_at","updated_at") VALUES (1,2,'test',20.0,'$','cash','joker','never mess with the joker',NULL,NULL),
 (2,1,'This is a description',20.0,'$','Cheque','Harley Quinn','Stay away from Harley Quinn',NULL,NULL),
 (3,2,'sdfsadf',4234.0,'$','Cash','fsdaf','asdfsadfaf',NULL,NULL),
 (4,1,'Test Sum',20.0,'$','Cash','fsfasf','sfasdf','2019-12-05 15:08:25',NULL),
 (5,1,'test',423.0,'$','Cash','','','2019-12-08 13:45:42',NULL);
INSERT INTO "sale_items" ("id","item_id","item_properties_id","sale_id","quantity","discount","returned_quantity","created_at","updated_at") VALUES (1,1,2,1,5,0.0,0,'2019-12-05 17:10:19',NULL),
 (2,3,5,2,6,0.0,0,'2019-12-05 17:11:27',NULL),
 (3,2,3,3,5,0.0,0,'2019-12-05 17:19:53',NULL),
 (4,2,3,4,3,0.0,0,'2019-12-05 17:20:02',NULL),
 (5,4,7,5,1,0.0,0,'2019-12-05 17:20:19',NULL),
 (6,3,5,6,5,0.0,0,'2019-12-05 17:30:19',NULL),
 (7,4,7,7,3,0.0,0,'2019-12-05 17:30:30',NULL),
 (8,2,3,7,2,0.0,0,'2019-12-05 17:30:30',NULL),
 (9,1,2,8,5,0.0,0,'2019-12-05 17:30:38',NULL),
 (10,3,5,9,12,0.0,0,'2019-12-05 17:30:48',NULL),
 (11,2,3,10,23,0.0,0,'2019-12-05 17:31:08',NULL),
 (12,3,5,11,8,0.0,0,'2019-12-06 08:39:49',NULL),
 (13,4,7,12,1,0.0,0,'2019-12-07 08:25:39',NULL),
 (14,1,2,12,20,0.0,0,'2019-12-07 08:25:39',NULL),
 (15,3,5,13,5,0.0,0,'2019-12-08 08:35:21',NULL),
 (16,3,5,14,90,0.0,0,'2019-12-08 14:59:43',NULL),
 (17,2,3,15,1,0.0,1,'2019-12-08 15:18:20',NULL),
 (18,2,3,17,8,0.0,3,'2019-12-09 17:36:03',NULL),
 (19,1,2,18,1,0.0,0,'2019-12-09 17:41:03',NULL),
 (20,2,3,19,0,0.0,0,'2019-12-09 18:08:23',NULL),
 (21,1,2,20,5,0.0,4,'2019-12-10 09:46:29',NULL),
 (22,1,2,21,5,0.0,4,'2019-12-10 09:47:00',NULL),
 (23,2,3,22,5,0.0,5,'2019-12-10 10:01:35',NULL),
 (24,1,2,23,3,0.0,0,'2019-12-14 11:04:55',NULL),
 (25,2,3,23,1,0.0,1,'2019-12-14 11:04:56',NULL),
 (26,1,2,24,1,0.0,1,'2019-12-16 11:11:59',NULL),
 (27,2,3,24,1,0.0,0,'2019-12-16 11:12:00',NULL),
 (28,3,5,24,3,0.0,0,'2019-12-16 11:12:00',NULL),
 (29,4,7,24,5,0.0,2,'2019-12-16 11:12:00',NULL),
 (30,2,3,25,6,0.0,0,'2019-12-16 14:42:18',NULL),
 (31,1,2,26,4,0.0,0,'2019-12-16 14:42:44',NULL),
 (32,1,2,27,10,0.0,0,'2019-12-16 14:43:06',NULL),
 (33,1,2,28,5,0.0,0,'2019-12-16 14:45:59',NULL),
 (34,1,2,29,100,0.0,0,'2019-12-20 14:44:03',NULL),
 (35,2,3,30,7,0.0,0,'2019-12-20 18:09:31',NULL),
 (36,4,7,31,5,0.0,0,'2019-12-24 14:00:26',NULL),
 (37,3,5,32,1,0.0,1,'2019-12-25 16:29:46',NULL),
 (38,1,2,33,5,0.0,3,'2019-12-26 08:48:14',NULL);
INSERT INTO "incomes" ("id","type_id","description","amount","currency","payment_type","taken_from","notes","created_at","updated_at") VALUES (1,3,'Testttt',30.0,'LL','Cash','Batman','batman came for the rescue',NULL,NULL),
 (2,2,'rasrasrasera',4234.0,'LL','Cash','','','2019-12-08 14:03:29',NULL);
INSERT INTO "sales" ("id","customer_id","discount","tax_id","conversion_rate","total_amount","currency","paid_amount","is_official","type","created_at","updated_at") VALUES (1,1,0.0,1,1500.0,7500.0,'LL',0.0,1,'Cash','2019-12-05 17:10:18',NULL),
 (2,1,0.0,1,1500.0,42000.0,'LL',0.0,1,'Cash','2019-12-04 17:11:27',NULL),
 (3,6,0.0,1,1500.0,25000.0,'LL',0.0,1,'Cash','2019-12-03 17:19:53',NULL),
 (4,1,0.0,1,1500.0,15000.0,'LL',0.0,1,'Cash','2019-12-02 17:20:02',NULL),
 (5,3,0.0,1,1500.0,4500.0,'LL',0.0,1,'Cash','2019-12-01 17:20:18',NULL),
 (6,1,0.0,1,1500.0,35000.0,'LL',0.0,1,'Cash','2019-11-30 17:30:19',NULL),
 (7,1,0.0,1,1500.0,23500.0,'LL',0.0,1,'Cash','2019-11-29 17:30:30',NULL),
 (8,1,0.0,1,1500.0,7500.0,'LL',0.0,1,'Cash','2019-11-28 17:30:38',NULL),
 (9,1,0.0,1,1500.0,84000.0,'LL',0.0,1,'Cash','2019-12-05 17:30:47',NULL),
 (10,1,0.0,1,1500.0,115000.0,'LL',25000.0,1,'Cash','2019-12-05 17:31:07',NULL),
 (11,1,0.0,1,1500.0,56000.0,'LL',0.0,1,'Cash','2019-12-06 08:39:49',NULL),
 (12,1,0.0,1,1500.0,34500.0,'LL',0.0,1,'Cash','2019-12-07 08:25:39',NULL),
 (13,1,3000.0,1,1500.0,32000.0,'LL',32000.0,1,'Cash','2019-12-08 08:35:21',NULL),
 (14,1,0.0,1,1500.0,630000.0,'LL',530000.0,1,'Cash','2019-12-08 14:59:43',NULL),
 (15,1,0.0,1,1500.0,0.0,'LL',0.0,1,'Cash','2019-12-08 15:18:20',NULL),
 (16,1,0.0,1,1500.0,7500.0,'LL',0.0,1,'Cash','2019-12-09 17:35:14',NULL),
 (17,1,0.0,1,1500.0,25000.0,'LL',25000.0,1,'Cash','2019-12-09 17:36:03',NULL),
 (18,1,0.0,1,1500.0,1500.0,'LL',1500.0,1,'Cash','2019-12-09 17:41:03',NULL),
 (19,1,0.0,1,1500.0,0.0,'LL',0.0,1,'Cash','2019-12-09 18:08:23',NULL),
 (20,1,1500.0,1,1500.0,0.0,'LL',0.0,1,'Cash','2019-12-10 09:46:28',NULL),
 (21,1,0.0,1,1500.0,1.0,'$',1.0,1,'Cash','2019-12-10 09:47:00',NULL),
 (22,1,0.0,1,1500.0,0.0,'LL',0.0,1,'Cash','2019-12-10 10:01:35',NULL),
 (23,1,4500.0,1,1500.0,0.0,'LL',0.0,1,'Cash','2019-12-14 11:04:55',NULL),
 (24,1,0.0,1,1500.0,39500.0,'LL',39500.0,1,'Cash','2019-12-16 11:11:59',NULL),
 (25,1,0.0,1,1500.0,30000.0,'LL',0.0,1,'Cash','2019-12-16 14:42:18',NULL),
 (26,1,0.0,1,1500.0,6000.0,'LL',0.0,1,'Cash','2019-12-16 14:42:44',NULL),
 (27,1,0.0,1,1500.0,15000.0,'LL',0.0,1,'Cash','2019-12-16 14:43:06',NULL),
 (28,1,0.0,1,1500.0,7500.0,'LL',0.0,1,'Cash','2019-12-16 14:45:59',NULL),
 (29,1,0.0,1,1500.0,150000.0,'LL',0.0,1,'Cash','2019-12-20 14:44:02',NULL),
 (30,1,0.0,1,1500.0,35000.0,'LL',35000.0,1,'Cash','2019-12-20 18:09:30',NULL),
 (31,1,0.0,1,1500.0,22500.0,'LL',0.0,1,'Cash','2019-12-24 14:00:25',NULL),
 (32,1,0.0,1,1500.0,0.0,'LL',0.0,1,'Cash','2019-12-25 16:29:46',NULL),
 (33,1,0.0,1,1500.0,3000.0,'LL',3000.0,1,'Cash','2019-12-26 08:48:14',NULL);
INSERT INTO "item_property_history" ("item_id","property_id") VALUES (1,2),
 (2,3),
 (3,5),
 (4,7);
INSERT INTO "tax" ("id","name","percentage") VALUES (1,'VAT',11);
INSERT INTO "item_categories" ("id","name") VALUES (2,'The Joker'),
 (4,'sdfasdfa'),
 (5,'fasdfasdfasd'),
 (6,'fsdfasdfasd');
INSERT INTO "income_types" ("id","name") VALUES (2,'Type1'),
 (3,'Type2'),
 (4,'FSDFAF');
INSERT INTO "expense_types" ("id","name") VALUES (1,'Traveling'),
 (2,'Joker stealing from us'),
 (3,'fsdfasdfasdfasdfasdfasdfasdf');
COMMIT;
