package control;

import database.DataAdapter;
import model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierController {

    // Add a new supplier
    public void addSupplier(Supplier supplier) throws SQLException {
        String query = "INSERT INTO Suppliers (SuppName, ContactP, Phone, Address, Status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DataAdapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, supplier.getSuppName());
            stmt.setString(2, supplier.getContactP());
            stmt.setString(3, supplier.getPhone());
            stmt.setString(4, supplier.getAddress());
            stmt.setString(5, supplier.getStatus());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error adding supplier: " + e.getMessage(), e);
        }
    }

    // Get a supplier by ID
    public Supplier getSupplier(int suppID) throws SQLException {
        String query = "SELECT * FROM Suppliers WHERE SuppID = ?";
        try (Connection conn = DataAdapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, suppID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapSupplier(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error retrieving supplier: " + e.getMessage(), e);
        }
        return null;
    }

    // Update an existing supplier
    public void updateSupplier(Supplier supplier) throws SQLException {
        String query = "UPDATE Suppliers SET SuppName = ?, ContactP = ?, Phone = ?, Address = ?, Status = ? WHERE SuppID = ?";
        try (Connection conn = DataAdapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, supplier.getSuppName());
            stmt.setString(2, supplier.getContactP());
            stmt.setString(3, supplier.getPhone());
            stmt.setString(4, supplier.getAddress());
            stmt.setString(5, supplier.getStatus());
            stmt.setInt(6, supplier.getSuppID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error updating supplier: " + e.getMessage(), e);
        }
    }

    // Delete a supplier
    public void deleteSupplier(int suppID) throws SQLException {
        String query = "DELETE FROM Suppliers WHERE SuppID = ?";
        try (Connection conn = DataAdapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, suppID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error deleting supplier: " + e.getMessage(), e);
        }
    }

    // List all suppliers
    public List<Supplier> getAllSuppliers() throws SQLException {
        List<Supplier> suppliers = new ArrayList<>();
        String query = "SELECT * FROM Suppliers";
        try (Connection conn = DataAdapter.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                suppliers.add(mapSupplier(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Error listing suppliers: " + e.getMessage(), e);
        }
        return suppliers;
    }

    // Helper method to map a ResultSet to a Supplier object
    private Supplier mapSupplier(ResultSet rs) throws SQLException {
        Supplier supplier = new Supplier();
        supplier.setSuppID(rs.getInt("SuppID"));
        supplier.setSuppName(rs.getString("SuppName"));
        supplier.setContactP(rs.getString("ContactP"));
        supplier.setPhone(rs.getString("Phone"));
        supplier.setAddress(rs.getString("Address"));
        supplier.setStatus(rs.getString("Status"));
        return supplier;
    }
}
