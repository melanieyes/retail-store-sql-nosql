package model;

public class BestSellingProduct {
    private String name;
    private String category;
    private double price;
    private int sales;
    private double revenue;

    public BestSellingProduct(String name, String category, double price, int sales, double revenue) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.sales = sales;
        this.revenue = revenue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
}
