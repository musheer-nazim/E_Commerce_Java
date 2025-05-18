import db.Database;
import models1.cart.Cart;
import models1.order.Order;
import models1.product.Product;
import models1.user.*;
import service.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RetailCommercePlatform {

    static Scanner scanner = new Scanner(System.in);
    static List<User> users = new ArrayList<>();
    static List<Product> products = new ArrayList<>();
    static List<Order> orders = new ArrayList<>();
    static List<Supplier> suppliers = new ArrayList<>();

    static AuthService authService = new AuthService();
    static CustomerService customerService = new CustomerService();
    static SupplierService supplierService = new SupplierService();
    static InventoryService inventoryService = new InventoryService();

    static User loggedInUser = null;

    public static void main(String[] args) {
        loadUsersFromDB();
        loadProductsFromDB();

        while (true) {
            System.out.println("\nWelcome to the E-Commerce Platform");
            System.out.println("1. Login");
            System.out.println("2. Register as Customer");
            System.out.println("3. Exit");
            System.out.print("Select option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> login();
                case 2 -> CustomerService.registerCustomer(users,scanner);
                case 3 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }

    }



    private static void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        loggedInUser = authService.login(username, password);
        if (loggedInUser == null) {
            System.out.println("Invalid credentials.");
            return;
        }

        if (loggedInUser instanceof Admin) adminLoop();
        else if (loggedInUser instanceof Supplier)
            supplierService.manageOwnProducts((Supplier) loggedInUser, products, scanner);
        else
            customerService.customerMenu(new Cart(), products, orders, loggedInUser, scanner);
    }

    private static void adminLoop() {
        while (true) {
            System.out.println("\nAdmin Menu");
            System.out.println("1. Inventory Management");
            System.out.println("2. Supplier Management");
            System.out.println("3. Logout");
            System.out.print("Select option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> inventoryService.showInventoryMenu(products, suppliers, scanner);
                case 2 -> supplierService.showSupplierMenu(suppliers, users, scanner);
                case 3 -> {
                    loggedInUser = null;
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void loadUsersFromDB() {
        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");
                switch (role) {
                    case "admin" -> users.add(new Admin(username, password));
                    case "customer" -> users.add(new Customer(username, password));
                    case "supplier" -> {
                        Supplier supplier = new Supplier(username, password);
                        users.add(supplier);
                        suppliers.add(supplier);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to load users: " + e.getMessage());
        }
    }

    private static void loadProductsFromDB() {
        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(
                    "SELECT p.name, p.price, p.stock, u.username " +
                            "FROM products p LEFT JOIN users u ON p.supplier_id = u.id"
            );
            while (rs.next()) {
                Product p = new Product(rs.getString("name"), rs.getDouble("price"), rs.getInt("stock"));
                String supplierUsername = rs.getString("username");
                for (Supplier s : suppliers) {
                    if (s.getUsername().equals(supplierUsername)) {
                        p.setSupplier(s);
                        break;
                    }
                }
                products.add(p);
            }
        } catch (Exception e) {
            System.out.println("Failed to load products: " + e.getMessage());
        }
    }
}
