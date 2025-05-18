package service;

import db.Database;
import models1.product.Product;
import models1.user.Supplier;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class InventoryService {

    public void viewProducts() {
        try (Connection conn = Database.getConnection()) {
            String query = "SELECT p.name, p.price, p.stock, u.username AS supplier " +
                    "FROM products p LEFT JOIN users u ON p.supplier_id = u.id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("\nProduct Inventory:");
            while (rs.next()) {
                System.out.printf("- %s ($%.2f) | Stock: %d | Supplier: %s\n",
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("supplier"));

            }
        } catch (SQLException e) {
            System.out.println("Failed to load products: " + e.getMessage());

        }
    }

    public void addProduct(List<Supplier> suppliers, Scanner scanner) {
        try (Connection conn = Database.getConnection()) {
            System.out.print("Enter product name: ");
            String name = scanner.nextLine();

            System.out.print("Enter price: ");
            double price = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter stock quantity: ");
            int stock = Integer.parseInt(scanner.nextLine());

            System.out.println("Available Suppliers:");
            for (int i = 0; i < suppliers.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, suppliers.get(i).getUsername());
            }

            System.out.print("Choose supplier number: ");
            int choice = Integer.parseInt(scanner.nextLine()) - 1;

            if (choice < 0 || choice >= suppliers.size()) {
                System.out.println("Invalid supplier.");
                return;
            }

            Supplier selectedSupplier = suppliers.get(choice);

            String sql = "INSERT INTO products (name, price, stock, supplier_id) VALUES (?, ?, ?, " +
                    "(SELECT id FROM users WHERE username = ?))";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, stock);
            pstmt.setString(4, selectedSupplier.getUsername());

            pstmt.executeUpdate();
            System.out.println("Product added successfully.");

        } catch (Exception e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    public void assignSupplier(List<Product> products, List<Supplier> suppliers, Scanner scanner) {
        try (Connection conn = Database.getConnection()) {
            // Load products from DB to show choices
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM products");

            List<Integer> productIds = new java.util.ArrayList<>();
            System.out.println("\nAvailable Products:");
            while (rs.next()) {
                int id = rs.getInt("id");
                productIds.add(id);
                System.out.printf("%d. %s\n", id, rs.getString("name"));
            }

            System.out.print("Enter product ID to assign supplier: ");
            int productId = Integer.parseInt(scanner.nextLine());

            if (!productIds.contains(productId)) {
                System.out.println("Invalid product ID.");
                return;
            }

            for (int i = 0; i < suppliers.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, suppliers.get(i).getUsername());
            }

            System.out.print("Choose supplier number: ");
            int sIndex = Integer.parseInt(scanner.nextLine()) - 1;

            if (sIndex < 0 || sIndex >= suppliers.size()) {
                System.out.println("Invalid supplier.");
                return;
            }

            String updateSql = "UPDATE products SET supplier_id = (SELECT id FROM users WHERE username = ?) WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateSql);
            pstmt.setString(1, suppliers.get(sIndex).getUsername());
            pstmt.setInt(2, productId);

            pstmt.executeUpdate();
            System.out.println("Supplier assigned to product successfully.");


        } catch (Exception e) {
            System.out.println("Failed to assign supplier: " + e.getMessage());
        }
    }

    public void showInventoryMenu(List<Product> products, List<Supplier> suppliers, Scanner scanner) {
        System.out.println("\nInventory Management");
        System.out.println("1. View Products");
        System.out.println("2. Add Product");
        System.out.println("3. Assign Supplier to Product");
        System.out.print("Select option: ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1 -> viewProducts();
            case 2 -> addProduct(suppliers, scanner);
            case 3 -> assignSupplier(products, suppliers, scanner);
            default -> System.out.println("Invalid option.");
        }
    }

}
