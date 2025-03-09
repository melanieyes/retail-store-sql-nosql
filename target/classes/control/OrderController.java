package control;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import database.MongoDBConnection;
import model.Order;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class OrderController {
    private final MongoCollection<Document> orderCollection;

    public OrderController() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.orderCollection = database.getCollection("orders");
    }

    // Fetch all orders
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        for (Document doc : orderCollection.find()) {
            Order order = new Order(
                    doc.getObjectId("_id"),
                    doc.getObjectId("CustID"),
                    doc.getDate("OrdDate"),
                    doc.getString("Status"),
                    getNumericValue(doc, "TotAmt")
            );
            orders.add(order);
        }
        return orders;
    }

    // Add a new order
    public void addOrder(Order order) {
        Document doc = new Document("CustID", order.getCustId())
                .append("OrdDate", order.getOrdDate())
                .append("Status", order.getStatus())
                .append("TotAmt", order.getTotAmt());
        orderCollection.insertOne(doc);
    }

    // Delete an order by ID
    public void deleteOrderById(String orderId) {
        orderCollection.deleteOne(new Document("_id", new ObjectId(orderId)));
    }

    // Helper method to extract numeric values (handles Integer and Double)
    private double getNumericValue(Document doc, String key) {
        Object value = doc.get(key);
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof Double) {
            return (Double) value;
        } else {
            throw new IllegalArgumentException("Unexpected numeric type: " + value.getClass());
        }
    }
}
