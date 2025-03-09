package control;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import database.MongoDBConnection;
import model.OrderItem;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class OrderItemController {
    private final MongoCollection<Document> collection;

    public OrderItemController() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.collection = database.getCollection("order_items");
    }

    // Fetch all order items
    public List<OrderItem> getAllOrderItems() {
        List<OrderItem> items = new ArrayList<>();
        for (Document doc : collection.find()) {
            OrderItem item = convertDocumentToOrderItem(doc);
            items.add(item);
        }
        return items;
    }

    // Fetch order items by order ID
    public List<OrderItem> getOrderItemsByOrderId(ObjectId orderId) {
        List<OrderItem> items = new ArrayList<>();
        for (Document doc : collection.find(new Document("OrderID", orderId))) {
            OrderItem item = convertDocumentToOrderItem(doc);
            items.add(item);
        }
        return items;
    }

    // Add a new order item
    public void addOrderItem(OrderItem item) {
        Document doc = new Document("OrderID", item.getOrderId())
                .append("ProdsName", item.getProdsName())
                .append("Qty", item.getQty())
                .append("UnitPrice", item.getUnitPrice())
                .append("TotalPrice", item.getTotalPrice());
        collection.insertOne(doc);
    }

    // Delete order item by ID
    public void deleteOrderItemById(ObjectId id) {
        collection.deleteOne(new Document("_id", id));
    }

    // Utility method to convert MongoDB Document to OrderItem object
    private OrderItem convertDocumentToOrderItem(Document doc) {
        OrderItem item = new OrderItem();
        item.setId(doc.getObjectId("_id"));
        item.setOrderId(doc.getObjectId("OrderID"));
        item.setProdsName(doc.getString("ProdsName"));
        item.setQty(doc.getInteger("Qty"));
        item.setUnitPrice(doc.getDouble("UnitPrice"));
        item.setTotalPrice(doc.getDouble("TotalPrice"));
        return item;
    }
}
