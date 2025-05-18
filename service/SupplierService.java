
package service;

import models1.product.Product;
import models1.user.Supplier;
import models1.user.User;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class SupplierService {

    public void manageOwnProducts(Supplier supplier, List<Product> products, Scanner scanner) {
        while (true) {
            System.out.println("\nSupplier Menu");
            System.out.println("1. View My Products");
            System.out.println("2. Edit My Product");
            System.out.println("3. Logout");
            System.out.print("Select option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("\nYour Products:");
                    for (Product p : products) {
                        if (p.getSupplier() != null &&
                                p.getSupplier().getUsername().equals(supplier.getUsername())) {
                            System.out.println(p);
                        }
                    }
                    break;

                case 2:
                    List<Product> owned = products.stream()
                            .filter(p -> p.getSupplier() != null &&
                                    p.getSupplier().getUsername().equals(supplier.getUsername()))
                            .toList();

                    if (owned.isEmpty()) {
                        System.out.println("No products found.");
                        break;
                    }

                    for (int i = 0; i < owned.size(); i++) {
                        System.out.printf("%d. %s\n", i + 1, owned.get(i));
                    }

                    System.out.print("Select product to edit: ");
                    int index = Integer.parseInt(scanner.nextLine()) - 1;

                    if (index >= 0 && index < owned.size()) {
                        Product selected = owned.get(index);

                        System.out.print("New Price: ");
                        selected.setPrice(Double.parseDouble(scanner.nextLine()));
                        System.out.print("New Stock: ");
                        selected.setStock(Integer.parseInt(scanner.nextLine()));

                        // TODO: Also update in DB
                        try (Connection conn = db.Database.getConnection()) {
                            String sql = "UPDATE products SET price = ?, stock = ? WHERE name = ?";
                            var pstmt = conn.prepareStatement(sql);
                            pstmt.setDouble(1, selected.getPrice());
                            pstmt.setInt(2, selected.getStock());
                            pstmt.setString(3, selected.getName());
                            pstmt.executeUpdate();
                            System.out.println("Product updated in database.");
                        } catch (Exception e) {
                            System.out.println("Failed to update product in database: " + e.getMessage());
                        }

                    } else {
                        System.out.println("Invalid selection.");
                    }
                    break;

                case 3:
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public void showSupplierMenu(List<Supplier> suppliers, List<User> users, Scanner scanner) {
        System.out.println("\nSupplier Management");
        System.out.println("1. View Suppliers");
        System.out.println("2. Add Supplier");
        System.out.print("Select option: ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1 -> {
                System.out.println("\nRegistered Suppliers:");
                for (Supplier s : suppliers) {
                    System.out.println("- " + s.getUsername());
                }
            }
            case 2 -> {
                System.out.print("Enter new supplier username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                // Check for duplicates first
                for (User user : users) {
                    if (user.getUsername().equalsIgnoreCase(username)) {
                        System.out.println("Username already exists. Choose a different one.");
                        return;
                    }
                }

                Supplier newSupplier = new Supplier(username, password);
                suppliers.add(newSupplier);
                users.add(newSupplier);

                // Save to DB
                try (Connection conn = db.Database.getConnection()) {
                    String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'supplier')";
                    var pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, username);
                    pstmt.setString(2, password);
                    pstmt.executeUpdate();
                    System.out.println("Supplier added successfully and stored in database.");
                } catch (Exception e) {
                    System.out.println("Error saving supplier to database: " + e.getMessage());
                }
            }
        }
    }
}
