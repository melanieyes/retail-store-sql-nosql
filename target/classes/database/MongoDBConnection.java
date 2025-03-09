package database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {

    private static MongoClient mongoClient = null;
    private static MongoDatabase database = null;

    // MongoDB URI (replace <username> with your MongoDB Atlas username)
    private static final String URI = "mongodb+srv://melanie:root@cluster0.rg3hy.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    // Method to connect to MongoDB
    public static void connect() {
        try {
            if (mongoClient == null) {
                mongoClient = MongoClients.create(URI);
                database = mongoClient.getDatabase("store");
                System.out.println("Connected to MongoDB database successfully.");
            }
        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to get the database
    public static MongoDatabase getDatabase() {
        if (database == null) {
            connect(); // Ensure connection is made before returning the database
        }
        return database;
    }
}
