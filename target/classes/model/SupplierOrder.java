package model;

public class SupplierOrder {
    private int suppOrdID;
    private int suppID;
    private java.sql.Date ordDate;
    private double totCost;
    private int statusID;

    // Getters and Setters
    public int getSuppOrdID() { return suppOrdID; }
    public void setSuppOrdID(int suppOrdID) { this.suppOrdID = suppOrdID; }

    public int getSuppID() { return suppID; }
    public void setSuppID(int suppID) { this.suppID = suppID; }

    public java.sql.Date getOrdDate() { return ordDate; }
    public void setOrdDate(java.sql.Date ordDate) { this.ordDate = ordDate; }

    public double getTotCost() { return totCost; }
    public void setTotCost(double totCost) { this.totCost = totCost; }

    public int getStatusID() { return statusID; }
    public void setStatusID(int statusID) { this.statusID = statusID; }
}
