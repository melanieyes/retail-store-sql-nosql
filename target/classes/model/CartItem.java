package model;

public class CartItem {
    private int prodsID;
    private String name;
    private double price;
    private int quantity;

    public CartItem(int prodsID, String name, double price, int quantity) {
        this.prodsID = prodsID;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getProdsID() { return prodsID; }
    public void setProdsID(int prodsID) { this.prodsID = prodsID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    /**
     * Calculates the total cost for this cart item.
     *
     * @return total cost (price * quantity)
     */
    public double getTotalCost() {
        return price * quantity;
    }

    @Override
    public String toString() {
        return String.format("CartItem{prodsID=%d, name='%s', price=%.2f, quantity=%d, total=%.2f}",
                prodsID, name, price, quantity, getTotalCost());
    }
}
