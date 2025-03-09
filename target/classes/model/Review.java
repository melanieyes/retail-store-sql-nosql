package model;

import org.bson.types.ObjectId;
import java.util.Date;

public class Review {
    private ObjectId id;
    private ObjectId custId;
    private ObjectId prodsId;
    private int rating;
    private String reviewText;
    private Date reviewDate;

    // Default Constructor
    public Review() {}

    // Parameterized Constructor
    public Review(ObjectId id, ObjectId custId, ObjectId prodsId, int rating, String reviewText, Date reviewDate) {
        this.id = id;
        this.custId = custId;
        this.prodsId = prodsId;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
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

    public ObjectId getProdsId() {
        return prodsId;
    }

    public void setProdsId(ObjectId prodsId) {
        this.prodsId = prodsId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }
}
