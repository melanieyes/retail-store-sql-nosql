package view;

import control.SupplierController;
import model.Supplier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class SupplierView extends JPanel {
    private final DefaultTableModel tableModel;
    private final SupplierController supplierController;
    private JTable supplierTable;

    // Consistent color theme
    private static final Color PRIMARY_COLOR = new Color(0, 139, 139); // Dark cyan
    private static final Color TEXT_COLOR = Color.WHITE;

    public SupplierView() {
        supplierController = new SupplierController();
        setLayout(new BorderLayout());
        setBackground(PRIMARY_COLOR);

        // Title label
        JLabel titleLabel = new JLabel("Supplier Management", JLabel.CENTER);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Table for displaying supplier data
        String[] columnNames = {"Supplier ID", "Name", "Contact", "Phone", "Address", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        supplierTable = new JTable(tableModel);
        supplierTable.setBackground(Color.DARK_GRAY);
        supplierTable.setForeground(TEXT_COLOR);
        supplierTable.setFont(new Font("Monospaced", Font.PLAIN, 14));
        supplierTable.getTableHeader().setBackground(PRIMARY_COLOR);
        supplierTable.getTableHeader().setForeground(TEXT_COLOR);
        add(new JScrollPane(supplierTable), BorderLayout.CENTER);

        // Buttons for supplier actions
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(PRIMARY_COLOR);
        JButton addButton = createStyledButton("Add Supplier");
        JButton deleteButton = createStyledButton("Delete Supplier");
        JButton updateButton = createStyledButton("Update Supplier");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load supplier data when the panel is initialized
        loadSuppliers();

        // Add supplier functionality
        addButton.addActionListener(e -> addSupplier());

        // Delete supplier functionality
        deleteButton.addActionListener(e -> {
            int selectedRow = supplierTable.getSelectedRow();
            if (selectedRow >= 0) {
                int supplierId = (int) tableModel.getValueAt(selectedRow, 0); // Get Supplier ID from the selected row
                deleteSupplier(supplierId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a supplier to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Update supplier functionality
        updateButton.addActionListener(e -> {
            int selectedRow = supplierTable.getSelectedRow();
            if (selectedRow >= 0) {
                int supplierId = (int) tableModel.getValueAt(selectedRow, 0); // Get Supplier ID
                updateSupplier(supplierId, selectedRow); // Pass row index for pre-filled data
            } else {
                JOptionPane.showMessageDialog(this, "Please select a supplier to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        Color buttonColor = new Color(244, 187, 68); // Amber
        Color buttonTextColor = Color.WHITE;
        button.setBackground(buttonColor);
        button.setForeground(buttonTextColor);
        button.setFont(new Font("Dialog", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        button.setOpaque(true);
        return button;
    }

    private void loadSuppliers() {
        try {
            List<Supplier> suppliers = supplierController.getAllSuppliers();

            // Clear existing rows in the table
            tableModel.setRowCount(0);

            // Populate the table with supplier data
            for (Supplier supplier : suppliers) {
                Object[] rowData = {
                        supplier.getSuppID(),
                        supplier.getSuppName(),
                        supplier.getContactP(),
                        supplier.getPhone(),
                        supplier.getAddress(),
                        supplier.getStatus()
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading suppliers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addSupplier() {
        JTextField nameField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField statusField = new JTextField();

        Object[] fields = {
                "Supplier Name:", nameField,
                "Contact Person:", contactField,
                "Phone:", phoneField,
                "Address:", addressField,
                "Status:", statusField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add Supplier", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Supplier supplier = new Supplier();
                supplier.setSuppName(nameField.getText());
                supplier.setContactP(contactField.getText());
                supplier.setPhone(phoneField.getText());
                supplier.setAddress(addressField.getText());
                supplier.setStatus(statusField.getText());
                supplierController.addSupplier(supplier);
                loadSuppliers(); // Refresh table
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding supplier: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSupplier(int supplierId) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this supplier?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                supplierController.deleteSupplier(supplierId);
                loadSuppliers(); // Refresh table
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting supplier: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateSupplier(int supplierId, int selectedRow) {
        // Get current values from the selected row
        String currentName = (String) tableModel.getValueAt(selectedRow, 1);
        String currentContact = (String) tableModel.getValueAt(selectedRow, 2);
        String currentPhone = (String) tableModel.getValueAt(selectedRow, 3);
        String currentAddress = (String) tableModel.getValueAt(selectedRow, 4);
        String currentStatus = (String) tableModel.getValueAt(selectedRow, 5);

        // Input fields pre-filled with current values
        JTextField nameField = new JTextField(currentName);
        JTextField contactField = new JTextField(currentContact);
        JTextField phoneField = new JTextField(currentPhone);
        JTextField addressField = new JTextField(currentAddress);
        JTextField statusField = new JTextField(currentStatus);

        Object[] fields = {
                "Supplier Name:", nameField,
                "Contact Person:", contactField,
                "Phone:", phoneField,
                "Address:", addressField,
                "Status:", statusField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Update Supplier", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Supplier supplier = new Supplier();
                supplier.setSuppID(supplierId);
                supplier.setSuppName(nameField.getText());
                supplier.setContactP(contactField.getText());
                supplier.setPhone(phoneField.getText());
                supplier.setAddress(addressField.getText());
                supplier.setStatus(statusField.getText());
                supplierController.updateSupplier(supplier); // Update supplier in the database
                loadSuppliers(); // Refresh table
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating supplier: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
