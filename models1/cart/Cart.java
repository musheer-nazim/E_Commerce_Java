package models1.cart;


import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem item) { items.add(item); }
    public void viewCart() {
        if (items.isEmpty()) System.out.println("Cart is empty.");
        else for (CartItem item : items) System.out.println(item);
    }
    public boolean isEmpty() { return items.isEmpty(); }
    public List<CartItem> getItems() { return new ArrayList<>(items); }
    public double getTotal() {
        double total = 0;
        for (CartItem item : items) total += item.getTotalPrice();
        return total;
    }
    public void clear() { items.clear(); }
}
