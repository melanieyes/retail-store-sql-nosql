package control;

import database.DataAdapter;
import model.ProdCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdCategoryController {
    // Add a new product category
    public void addCategory(ProdCategory category) throws SQLException {
        String query = "INSERT INTO ProdCateg (CatName) VALUES (?)";
        try (Connection conn = DataAdapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, category.getCatName());
            stmt.executeUpdate();
        }
    }

    // Update an existing category
    public void updateCategory(ProdCategory category) throws SQLException {
        String query = "UPDATE ProdCateg SET CatName = ? WHERE ProdCatID = ?";
        try (Connection conn = DataAdapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, category.getCatName());
            stmt.setInt(2, category.getProdCatID());
            stmt.executeUpdate();
        }
    }

    // Delete a category
    public void deleteCategory(int prodCatID) throws SQLException {
        String query = "DELETE FROM ProdCateg WHERE ProdCatID = ?";
        try (Connection conn = DataAdapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, prodCatID);
            stmt.executeUpdate();
        }
    }

    // Retrieve all categories
    public List<ProdCategory> getAllCategories() throws SQLException {
        List<ProdCategory> categories = new ArrayList<>();
        String query = "SELECT * FROM ProdCateg";
        try (Connection conn = DataAdapter.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                ProdCategory category = new ProdCategory();
                category.setProdCatID(rs.getInt("ProdCatID"));
                category.setCatName(rs.getString("CatName"));
                categories.add(category);
            }
        }
        return categories;
    }
}
