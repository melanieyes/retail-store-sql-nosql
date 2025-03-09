package control;

import database.DataAdapter;
import model.SupplierOrder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierOrderController {

    // Add a new supplier order
    public void addSupplierOrder(SupplierOrder supplierOrder) throws SQLException {
        String query = "INSERT INTO SuppOrder (SuppID, OrdDate, TotCost, StatusID) VALUES (?, ?, ?, ?)";
        try (Connection conn = DataAdapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, supplierOrder.getSuppID());
            stmt.setDate(2, supplierOrder.getOrdDate());
            stmt.setDouble(3, supplierOrder.getTotCost());
            stmt.setInt(4, supplierOrder.getStatusID());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                supplierOrder.setSuppOrdID(rs.getInt(1));
            }
        }
    }

    // Get a supplier order by ID
    public SupplierOrder getSupplierOrder(int suppOrdID) throws SQLException {
        String query = "SELECT * FROM SuppOrder WHERE SuppOrdID = ?";
        try (Connection conn = DataAdapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, suppOrdID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapSupplierOrder(rs);
            }
        }
        return null;
    }

    // Get all supplier orders
    public List<SupplierOrder> getAllSupplierOrders() throws SQLException {
        List<SupplierOrder> supplierOrders = new ArrayList<>();
        String query = "SELECT * FROM SuppOrder";

        try (Connection conn = DataAdapter.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                supplierOrders.add(mapSupplierOrder(rs));
            }
        }

        return supplierOrders;
    }

    // Update an existing supplier order
    public void updateSupplierOrder(SupplierOrder supplierOrder) throws SQLException {
        String query = "UPDATE SuppOrder SET SuppID = ?, OrdDate = ?, TotCost = ?, StatusID = ? WHERE SuppOrdID = ?";
        try (Connection conn = DataAdapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, supplierOrder.getSuppID());
            stmt.setDate(2, supplierOrder.getOrdDate());
            stmt.setDouble(3, supplierOrder.getTotCost());
            stmt.setInt(4, supplierOrder.getStatusID());
            stmt.setInt(5, supplierOrder.getSuppOrdID());
            stmt.executeUpdate();
        }
    }

    // Helper method to map a ResultSet to a SupplierOrder object
    private SupplierOrder mapSupplierOrder(ResultSet rs) throws SQLException {
        SupplierOrder supplierOrder = new SupplierOrder();
        supplierOrder.setSuppOrdID(rs.getInt("SuppOrdID"));
        supplierOrder.setSuppID(rs.getInt("SuppID"));
        supplierOrder.setOrdDate(rs.getDate("OrdDate"));
        supplierOrder.setTotCost(rs.getDouble("TotCost"));
        supplierOrder.setStatusID(rs.getInt("StatusID"));
        return supplierOrder;
    }
}
