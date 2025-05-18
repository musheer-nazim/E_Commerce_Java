Retail E-Commerce Platform (Console-Based Java Application)
============================================================

Overview
--------
This is a console-based e-commerce platform developed in Java for managing an online retail system.
The system supports three types of users: Admin, Supplier, and Customer. It includes user authentication,
inventory management, order handling, and supplier-product relationships.

Project Features
----------------
✔ Admin
  - View product inventory
  - Add new products
  - Assign suppliers to products
  - View and add suppliers

✔ Supplier
  - View only their assigned products
  - Edit price and stock of their products

✔ Customer
  - Register and login
  - Browse products
  - Add products to cart
  - Checkout
  - View order history

✔ General
  - SQLite database for persistent storage of users and products
  - Modular service-based architecture
  - Clean and testable code
  - Unit tests for core services

Folder Structure
----------------
/src
  ├── main
  │   ├── java
  │   │   ├── db/                 # Database connection and helper
  │   │   ├── models1/            # Domain model classes
  │   │   ├── models1/cart/       # Cart and CartItem
  │   │   ├── models1/order/      # Order class
  │   │   ├── models1/product/    # Product class
  │   │   ├── models1/user/       # User, Admin, Supplier, Customer
  │   │   ├── service/            # Business logic services
  │   │   └── RetailCommercePlatform.java  # Main class
  ├── test                       # Unit tests for services
/database
  └── retail_platform.db         # SQLite database

How to Run
----------
1. Make sure you have Java (17+) and SQLite JDBC driver.
2. Clone or download the repository.
3. Navigate to the project root directory.
4. Compile all Java files:
   javac -cp ".;sqlite-jdbc-<version>.jar" src/main/java/**/*.java
5. Run the main application:
   java -cp ".;sqlite-jdbc-<version>.jar" RetailCommercePlatform

*Replace <version> with the actual SQLite JDBC jar version.

Database Schema
---------------
Table: users
  - id INTEGER PRIMARY KEY AUTOINCREMENT
  - username TEXT UNIQUE
  - password TEXT
  - role TEXT (admin, supplier, customer)

Table: products
  - id INTEGER PRIMARY KEY AUTOINCREMENT
  - name TEXT
  - price REAL
  - stock INTEGER
  - supplier_id INTEGER (foreign key to users.id)

Example Logins
--------------
Admin:    username = admin      password = admin123
Supplier: username = bob   password = supp123
Customer: username = alice   password = cust123

Contributors
------------
- Musheer Nazim Mohammad
- Qaria Ammara Naeem
- Zubair Mohammed

