
package models1.order;

import models1.cart.CartItem;
import models1.user.User;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private User user;
    private List<CartItem> items;
    private double total;
    private LocalDateTime timestamp;

    public Order(User user, List<CartItem> items, double total) {
        this.user = user;
        this.items = items;
        this.total = total;
        this.timestamp = LocalDateTime.now();
    }

    public User getUser() { return user; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Order Total: $" + total + " | Items: ");
        for (CartItem item : items) sb.append(item.toString()).append(", ");
        return sb.toString();
    }
}
