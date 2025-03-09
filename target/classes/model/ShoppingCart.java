package model;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private final List<CartItem> items;

    public ShoppingCart() {
        this.items = new ArrayList<>();
    }


    public void addItem(CartItem newItem) {
        for (CartItem item : items) {
            if (item.getProdsID() == newItem.getProdsID()) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                return; // exist
            }
        }
        items.add(newItem);
    }
    public void removeProduct(int prodsID) {
        items.removeIf(item -> item.getProdsID() == prodsID);
    }


    public double calculateTotal() {
        return items.stream().mapToDouble(CartItem::getTotalCost).sum();
    }


    public void clear() {
        items.clear();
    }

    public List<CartItem> getItems() {
        return items;
    }
}
