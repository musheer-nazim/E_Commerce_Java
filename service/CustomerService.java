
package service;

import db.Database;
import models1.cart.Cart;
import models1.cart.CartItem;
import models1.order.Order;
import models1.product.Product;
import models1.user.Customer;
import models1.user.User;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class CustomerService {

    private final OrderService orderService = new OrderService();

    public void customerMenu(Cart cart, List<Product> products, List<Order> orders, User user, Scanner scanner) {
        while (true) {
            System.out.println("\nCustomer Menu");
            System.out.println("1. Browse Products");
            System.out.println("2. View Cart");
            System.out.println("3. Checkout");
            System.out.println("4. View Order History");
            System.out.println("5. Logout");
            System.out.print("Select option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    browseProducts(cart, products, scanner);
                    break;
                case 2:
                    cart.viewCart();
                    break;
                case 3:
                    orderService.checkout(cart, orders, user, scanner);
                    break;
                case 4:
                    orderService.viewOrderHistory(orders, user);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void browseProducts(Cart cart, List<Product> products, Scanner scanner) {
        for (int i = 0; i < products.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, products.get(i));
        }
        System.out.print("Select product to add to cart (0 to cancel): ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;
        if (index >= 0 && index < products.size()) {
            Product p = products.get(index);
            System.out.print("Quantity: ");
            int qty = Integer.parseInt(scanner.nextLine());
            if (qty <= p.getStock()) {
                cart.addItem(new CartItem(p, qty));
                p.reduceStock(qty);
                System.out.println("Added to cart.");
            } else {
                System.out.println("Not enough stock.");
            }
        }
    }

    public static void registerCustomer(List<User> users, Scanner scanner) {
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();

        // Check if username already exists
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                System.out.println("Username already exists. Please choose a different one.");
                return;
            }
        }

        System.out.print("Enter a password: ");
        String password = scanner.nextLine();

        // Create and save customer to memory
        Customer newCustomer = new Customer(username, password);
        users.add(newCustomer);

        // Optional: persist to DB
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'customer')";
            var pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            System.out.println("Customer registered successfully!");
        } catch (Exception e) {
            System.out.println("Error saving to database: " + e.getMessage());
        }
    }

}
