package model;

import org.bson.types.ObjectId;

public class OrderItem {
    private ObjectId id;
    private ObjectId orderId;
    private String prodsName;
    private int qty;
    private double unitPrice;
    private double totalPrice;

    // Getters and Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getOrderId() {
        return orderId;
    }

    public void setOrderId(ObjectId orderId) {
        this.orderId = orderId;
    }

    public String getProdsName() {
        return prodsName;
    }

    public void setProdsName(String prodsName) {
        this.prodsName = prodsName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
