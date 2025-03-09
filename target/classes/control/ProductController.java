package control;

import database.DataAdapter;
import model.Products;

import java.sql.*;
import java.util.*;


public class ProductController {
    // Add a new product
    public void addProduct(Products product) throws SQLException {
        String query = "INSERT INTO Products (ProdsName, ProdCatID, Price, QtyStock, SuppID) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DataAdapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, product.getProdsName());
            stmt.setInt(2, product.getProdCatID());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQtyStock());
            stmt.setInt(5, product.getSuppID());
            stmt.executeUpdate();
        }
    }

    // Get a product by ID
    public Products getProduct(int prodsID) throws SQLException {
        String query = "SELECT * FROM Products WHERE ProdsID = ?";
        try (Connection conn = DataAdapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, prodsID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapProduct(rs);
            }
        }
        return null;
    }

    // Update an existing product
    public void updateProduct(Products product) throws SQLException {
        String query = "UPDATE Products SET ProdsName = ?, ProdCatID = ?, Price = ?, QtyStock = ?, SuppID = ? WHERE ProdsID = ?";
        try (Connection conn = DataAdapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, product.getProdsName());
            stmt.setInt(2, product.getProdCatID());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQtyStock());
            stmt.setInt(5, product.getSuppID());
            stmt.setInt(6, product.getProdsID());
            stmt.executeUpdate();
        }
    }

    // Delete a product
    public void deleteProduct(int prodsID) throws SQLException {
        String query = "DELETE FROM Products WHERE ProdsID = ?";
        try (Connection conn = DataAdapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, prodsID);
            stmt.executeUpdate();
        }
    }

    // List all products
    public List<Products> getAllProducts() throws SQLException {
        List<Products> products = new ArrayList<>();
        String query = "SELECT * FROM Products";
        try (Connection conn = DataAdapter.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        }
        return products;
    }

    // Helper method to map a ResultSet to a Product object
    private Products mapProduct(ResultSet rs) throws SQLException {
        Products products = new Products();
        products.setProdsID(rs.getInt("ProdsID"));
        products.setProdsName(rs.getString("ProdsName"));
        products.setProdCatID(rs.getInt("ProdCatID"));
        products.setPrice(rs.getDouble("Price"));
        products.setQtyStock(rs.getInt("QtyStock"));
        products.setSuppID(rs.getInt("SuppID"));
        return products;
    }
}
