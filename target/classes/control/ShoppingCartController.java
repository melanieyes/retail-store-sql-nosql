package control;

import database.DataAdapter;
import database.MongoDBConnection;
import model.CartItem;
import model.Products;
import model.ShoppingCart;
import org.bson.Document;
import org.bson.types.ObjectId;
import view.ShoppingCartView;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShoppingCartController {
    private final ShoppingCart cart;
    private final ShoppingCartView view;

    public ShoppingCartController(ShoppingCartView view, ShoppingCart cart) {
        this.view = view;
        this.cart = cart;

        loadProducts();
        setupListeners();
    }

    private void loadProducts() {
        List<Products> products = new ArrayList<>();
        String query = "SELECT * FROM Products";

        try (Connection conn = DataAdapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Products product = new Products();
                product.setProdsID(rs.getInt("ProdsID"));
                product.setProdsName(rs.getString("ProdsName"));
                product.setPrice(rs.getDouble("Price"));
                product.setProdCatID(rs.getInt("ProdCatID"));
                product.setQtyStock(rs.getInt("QtyStock"));
                product.setSuppID(rs.getInt("SuppID"));
                products.add(product);
            }
            view.updateProductTable(products); // Update the product table with fetched products

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error loading products from MySQL: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupListeners() {
        view.addAddToCartListener(e -> addToCart());
        view.addConfirmOrderListener(e -> confirmOrder());
        view.addDeleteItemListener(e -> deleteItem()); // Add listener for delete button
    }

    private void handleCartVisibility() {
        if (cart.getItems().isEmpty()) {
            // If the cart is empty, reload the products
            loadProducts();  // Load product data and update the cart view
        } else {
            // If the cart has items, show them in the cart table
            view.updateCartTable(cart.getItems());
        }
    }

    private void addToCart() {
        int selectedRow = view.getSelectedProductRow();

        if (selectedRow >= 0) {
            int prodsID = view.getSelectedProductID(selectedRow);
            String name = view.getSelectedProductName(selectedRow);
            double price = view.getSelectedProductPrice(selectedRow);

            String quantityStr = JOptionPane.showInputDialog(view, "Enter quantity:");
            if (quantityStr == null || quantityStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(view, "Quantity is required. Please try again.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int quantity = Integer.parseInt(quantityStr.trim());
                if (quantity <= 0) {
                    throw new NumberFormatException("Quantity must be greater than 0.");
                }

                cart.addItem(new CartItem(prodsID, name, price, quantity));
                view.updateCartTable(cart.getItems());
                JOptionPane.showMessageDialog(view, "Product added to cart.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(view, "Invalid quantity. Please enter a valid number greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(view, "Please select a product to add to the cart.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void confirmOrder() {
        if (cart.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Your cart is empty. Please add products before confirming the order.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }


        Object[] userDetails = view.showOrderConfirmationDialog();

        if (userDetails != null) {
            String firstName = (String) userDetails[0];
            String lastName = (String) userDetails[1];
            String email = (String) userDetails[2];
            String address = (String) userDetails[3];
            String phone = (String) userDetails[4];

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            try {
                ObjectId orderId = saveOrderToMongo(firstName, lastName, email, address, phone, cart.calculateTotal(), cart.getItems());
                JOptionPane.showMessageDialog(view, "Order Confirmed!\nOrder ID: " + orderId + "\nTotal: $" + cart.calculateTotal(), "Success", JOptionPane.INFORMATION_MESSAGE);

                // Clear the cart
                cart.clear();
                view.clearCartTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Error confirming order: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteItem() {
        int selectedRow = view.getSelectedProductRow();

        if (selectedRow >= 0) {
            int productId = view.getSelectedProductID(selectedRow);
            cart.removeProduct(productId);
            handleCartVisibility();
            JOptionPane.showMessageDialog(view, "Product removed from cart.");
        } else {
            JOptionPane.showMessageDialog(view, "Please select a product to remove.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private ObjectId saveOrderToMongo(String firstName, String lastName, String email, String address, String phone, double total, List<CartItem> items) {
        var database = MongoDBConnection.getDatabase();
        var ordersCollection = database.getCollection("orders");
        var orderItemsCollection = database.getCollection("order_items");
        var customersCollection = database.getCollection("customers");


        Document customerDoc = new Document()
                .append("FName", firstName)
                .append("LName", lastName)
                .append("Email", email)  // Store email in MongoDB
                .append("Address", address)
                .append("Phone", phone)
                .append("JoinDate", new Date());

        customersCollection.insertOne(customerDoc);
        ObjectId customerId = customerDoc.getObjectId("_id");


        Document orderDoc = new Document()
                .append("CustID", customerId)
                .append("OrdDate", new Date())
                .append("Status", "Pending")
                .append("TotAmt", total);

        ordersCollection.insertOne(orderDoc);
        ObjectId orderId = orderDoc.getObjectId("_id");

        // Save each item in the order
        List<Document> orderItemsDocs = new ArrayList<>();
        for (CartItem item : items) {
            Document orderItemDoc = new Document()
                    .append("OrderID", orderId)
                    .append("ProdsName", item.getName())
                    .append("Qty", item.getQuantity())
                    .append("UnitPrice", item.getPrice())
                    .append("TotalPrice", item.getTotalCost());

            orderItemsDocs.add(orderItemDoc);
        }
        orderItemsCollection.insertMany(orderItemsDocs);


        updateProductStock(items);

        return orderId;
    }

    private void updateProductStock(List<CartItem> items) {
        var productsCollection = MongoDBConnection.getDatabase().getCollection("products");

        for (CartItem item : items) {
            productsCollection.updateOne(
                    new Document("prodsID", item.getProdsID()),
                    new Document("$inc", new Document("QtyStock", -item.getQuantity()))
            );
        }
    }
}
