package model;

import org.bson.types.ObjectId;
import java.util.Date;

public class Order {
    private ObjectId id;
    private ObjectId custId;
    private Date ordDate;
    private String status;
    private double totAmt;

    // Default Constructor
    public Order() {}

    // Parameterized Constructor
    public Order(ObjectId id, ObjectId custId, Date ordDate, String status, double totAmt) {
        this.id = id;
        this.custId = custId;
        this.ordDate = ordDate;
        this.status = status;
        this.totAmt = totAmt;
    }

    // Getters and Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getCustId() {
        return custId;
    }

    public void setCustId(ObjectId custId) {
        this.custId = custId;
    }

    public Date getOrdDate() {
        return ordDate;
    }

    public void setOrdDate(Date ordDate) {
        this.ordDate = ordDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotAmt() {
        return totAmt;
    }

    public void setTotAmt(double totAmt) {
        this.totAmt = totAmt;
    }
}