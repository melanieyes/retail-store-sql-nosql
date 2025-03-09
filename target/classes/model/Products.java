package model;

public class Products {
    private int prodsID;
    private String prodsName;
    private int prodCatID;
    private double price;
    private int qtyStock;
    private int suppID;

    // Getters and Setters
    public int getProdsID() { return prodsID; }
    public void setProdsID(int prodsID) { this.prodsID = prodsID; }

    public String getProdsName() { return prodsName; }
    public void setProdsName(String prodsName) { this.prodsName = prodsName; }

    public int getProdCatID() { return prodCatID; }
    public void setProdCatID(int prodCatID) { this.prodCatID = prodCatID; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQtyStock() { return qtyStock; }
    public void setQtyStock(int qtyStock) { this.qtyStock = qtyStock; }

    public int getSuppID() { return suppID; }
    public void setSuppID(int suppID) { this.suppID = suppID; }
}
