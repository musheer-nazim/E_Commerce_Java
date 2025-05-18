
package models1.product;

import models1.user.Supplier;

public class Product {
    private String name;
    private double price;
    private int stock;
    private Supplier supplier;

    public Product(String name, double price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public void reduceStock(int qty) { stock -= qty; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

    @Override
    public String toString() {
        return name + " ($" + price + ") | Stock: " + stock +
               (supplier != null ? " | Supplier: " + supplier.getUsername() : "");
    }
}
