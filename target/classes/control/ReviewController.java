package control;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import database.MongoDBConnection;
import model.Review;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class ReviewController {
    private final MongoCollection<Document> reviewCollection;

    // Constructor
    public ReviewController() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.reviewCollection = database.getCollection("reviews");
    }

    // Fetch all reviews
    public List<Review> getAllReviews() {
        List<Review> reviews = new ArrayList<>();
        for (Document doc : reviewCollection.find()) {
            Review review = new Review(
                    doc.getObjectId("id"),
                    doc.getObjectId("CustID"),
                    doc.getObjectId("ProdsID"),
                    doc.getInteger("Rating"),
                    doc.getString("ReviewText"),
                    doc.getDate("ReviewDate")
            );
            reviews.add(review);
        }
        return reviews;
    }

    // Add a new review
    public void addReview(Review review) {
        Document doc = new Document("CustID", review.getCustId())
                .append("ProdsID", review.getProdsId())
                .append("Rating", review.getRating())
                .append("ReviewText", review.getReviewText())
                .append("ReviewDate", review.getReviewDate());
        reviewCollection.insertOne(doc);
    }

    // Delete a review by Customer ID and Product ID
    public void deleteReviewByCustomerAndProductId(String custId, String prodsId) {
        try {
            Document query = new Document("CustID", new ObjectId(custId))
                    .append("ProdsID", new ObjectId(prodsId)); // Matching ID
            reviewCollection.deleteOne(query);
            System.out.println("Review deleted successfully for Customer ID: " + custId + " and Product ID: " + prodsId);
        } catch (Exception e) {
            System.err.println("Error deleting review: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
