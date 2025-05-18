
package service;

import models1.cart.Cart;
import models1.cart.CartItem;
import models1.order.Order;
import models1.user.User;

import java.util.List;
import java.util.Scanner;

public class OrderService {

    public void checkout(Cart cart, List<Order> orders, User user, Scanner scanner) {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        double total = cart.getTotal();
        System.out.printf("Total: $%.2f\n", total);
        System.out.print("Proceed to checkout? (yes/no): ");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("yes")) {
            orders.add(new Order(user, cart.getItems(), total));
            cart.clear();
            System.out.println("Order placed!");
        }
    }

    public void viewOrderHistory(List<Order> orders, User user) {
        System.out.println("\nYour Order History:");
        for (Order o : orders) {
            if (o.getUser().equals(user)) System.out.println(o);
        }
    }
}
