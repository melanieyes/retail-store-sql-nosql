package control;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import database.MongoDBConnection;
import model.Customer;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerController {
    private final MongoCollection<Document> customerCollection;

    // Constructor
    public CustomerController() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.customerCollection = database.getCollection("customers");
    }

    // Fetch all customers
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        for (Document doc : customerCollection.find()) {
            Customer customer = new Customer(
                    doc.getObjectId("_id"),
                    doc.getString("FName"),
                    doc.getString("LName"),
                    doc.getString("Email"),
                    doc.getString("Phone"),
                    doc.getString("Address"),
                    doc.getDate("JoinDate")
            );
            customers.add(customer);
        }
        return customers;
    }

    // Add a new customer
    public void addCustomer(Customer customer) {
        Document doc = new Document("FName", customer.getfName())
                .append("LName", customer.getlName())
                .append("Email", customer.getEmail())
                .append("Phone", customer.getPhone())
                .append("Address", customer.getAddress())
                .append("JoinDate", customer.getJoinDate());
        customerCollection.insertOne(doc);
    }

    // Delete a customer by ID
    public void deleteCustomer(ObjectId id) {
        customerCollection.deleteOne(new Document("_id", id));
    }
}
